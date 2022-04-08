import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class Lock {
    public static boolean acquireLock(String query,String transactionId) throws Exception{
        String tableName = getTableName(query);
        int checker=reader(tableName,transactionId);
        // 0 = lock is already taken
        // 1 = denied lock acquisition
        // 4 = can acquire lock

        switch (checker){
            case 0:
                System.out.println("lock already taken on table: "+tableName+" by transaction: "+transactionId);
                return true;
            case 1:
                System.out.println("Cannot provide lock on table: "+tableName+" because taken by: "+transactionId);

                //after 10 sec, it will try again to acquire lock.
//                TimeUnit.SECONDS.sleep(10);
//                Lock.acquireLock(query,transactionId);
                return false;
            case 4,5:
                writer(tableName,transactionId);
                System.out.println("Lock given to Transaction : "+transactionId+" on table: "+tableName);
                return true;
            default:
                return false;
            //do Nothing
        }
    }

    public static String getTableName(String query){
        String tableName = "";
        String[] words = query.toLowerCase(Locale.ROOT).split(" ");
        if(words[0].equalsIgnoreCase("select")){
            int indexOfFrom = Arrays.asList(words).indexOf("from");
            return words[indexOfFrom+1].replaceAll("[^a-zA-Z0-9]","");
        } else {
            tableName = words[2];
            return tableName.replaceAll("[^a-zA-Z0-9]","");
        }
    }

    private static void writer(String tableName,String transactionId) throws Exception{
        System.out.println("I am in writer.");
        File file = new File("./src/main/java/Transaction.txt");
        if(!file.exists()){
            file.createNewFile();
        }
        FileWriter fileWriter = new FileWriter(file,true);
        fileWriter.append(transactionId+";"+tableName+"\n");
        fileWriter.flush();
        fileWriter.close();
    }

    private static int reader(String tableName,String transactionId) throws Exception{
        System.out.println("I am in reader.");
        File file = new File("./src/main/java/Transaction.txt");
        if(!file.exists()){
            file.createNewFile();
            return 5;
        }


        BufferedReader bf = new BufferedReader(new FileReader(file));
        String line ="";

        if((line=bf.readLine()) == null){
            return 5;
        }

        do{
            int checker = lockChecker(tableName,transactionId,line);
            if( checker == 0) {
                System.out.println("lock is already taken.");
                return 0;
            } else if(checker == 1) {
                System.out.println("table acquired by another table.");
                return 1;
            }
        } while((line=bf.readLine()) != null);
        return 4;
    }

    private static int lockChecker(String tableName, String transactionId,String line){

        String[] transactionWords = line.split(";");
        int flag = 0;
        if(transactionWords[1].equalsIgnoreCase(tableName)){
            if(transactionWords[0].equalsIgnoreCase(transactionId)){
                //same table same transaction
                return 0;
            } else{
                //same table different transaction
                return 1;
            }
        } else {
            if(transactionWords[0].equalsIgnoreCase(transactionId)){
                //different table same transaction
                return 2;
            } else {
                //different table different transaction
                return 3;
            }
        }
    }

    public static void releaseLock(String tableName, String transactionId) throws Exception{
        File inFile = new File("./src/main/java/Transaction.txt");
        File tempFile = new File(inFile.getAbsolutePath()+".tmp");
        FileWriter fileWriter = new FileWriter(tempFile,true);
        if(!inFile.exists()){
            inFile.createNewFile();
        }
        BufferedReader bf = new BufferedReader(new FileReader(inFile));
        String line ="";
        boolean lockRemoved = false;
        while((line = bf.readLine()) != null){
            int checker = lockChecker(tableName,transactionId,line);
            if(checker == 0) {
                lockRemoved = true;
                continue;
            }
            fileWriter.append(line+"\n");
        }
        fileWriter.flush();
        fileWriter.close();
        tempFile.renameTo(inFile);

        if(lockRemoved){
            System.out.println("All lock removed.");
        }
    }
}
