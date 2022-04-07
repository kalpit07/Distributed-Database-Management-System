package Transaction;

import java.io.ObjectInputFilter;
import java.util.*;

/**
 * @author : Kishan Savaliya
 */
public class Transaction {

    //For storing query validation and type this kind:--- true;DDL
    public static HashMap<String, String> queryTable = new HashMap<String,String>();

    public static void startTransaction() throws Exception{
        String transactionId = Config.idGenerator();
        takeInputQuery(transactionId);
    }

    //Taking input in transaction module
    public static void takeInputQuery(String transactionId) throws Exception{

        boolean flag = true;
        Scanner sc = new Scanner(System.in);
        while(flag){
            System.out.println("Enter Query:");
            String query = sc.nextLine();
            Lock.acquireLock(query,transactionId);
            if(query.equalsIgnoreCase("commit;")){
                break;
            }
            makeHashmap(query);
        }
    }

    public static void makeHashmap(String query){

        boolean validation = true;
        String[] words = query.split(" ");
        if(Arrays.asList(Config.DDL).contains(words[0].toLowerCase())){
            if(validation){
                queryTable.put(query,"true;DDL");
            } else {
                queryTable.put(query,"false;DDL");
            }
        } else if(Arrays.asList(Config.DML).contains(words[0].toLowerCase())){
            if(validation){
                queryTable.put(query,"true;DML");
            } else {
                queryTable.put(query,"false;DML");
            }
        } else {
            //nothing
        }
        System.out.println(queryTable.get(query));

    }

    // public static void main(String[] args) throws Exception{
    //     takeInputQuery();
    //     startTransaction();
    // }

    public static void startExecution(){

        String[] keys = queryTable.keySet().toArray(new String[0]);

        //For seperating DLL queries
        ArrayList<String> DDL_query = new ArrayList<String>();
        boolean flag = false;
        for(String key : keys){
            String value = queryTable.get(key);
            String[] results = value.split(";");
            if(results[1].equalsIgnoreCase("ddl")){
                DDL_query.add(key);
            }
            if(results[0].equalsIgnoreCase("false")){
                flag = true;
                break;
            }
        }
        System.out.println(DDL_query);
        if(!flag){
            System.out.println("All queries are valid.");
        }
    }
}
