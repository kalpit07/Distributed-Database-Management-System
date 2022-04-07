package Transaction;

import java.io.File;
import java.io.FileWriter;
import java.util.*;

/**
* @author : Kishan Savaliya
*/

public class Lock {

    public static void acquireLock(String query,String transactionId) throws Exception{
        String tableName = getTableName(query);
        writer(tableName,transactionId);
        System.out.println(tableName);
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

    private static void writer(String tableName, String transactionId) throws Exception{

        File file = new File("./src/main/java/Transaction.txt");
        if(!file.exists()){
            file.createNewFile();
        }
        FileWriter fileWriter = new FileWriter(file,true);
        fileWriter.append(transactionId+"|"+tableName+"|"+System.currentTimeMillis()+"\n");
        fileWriter.flush();
        fileWriter.close();
    }

}