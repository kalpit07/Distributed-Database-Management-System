package Transaction;

import java.util.*;

/**
* @author : Kishan Savaliya
*/

public class Lock {

    public static void acquireLock(String query){
        String tableName = getTableName(query);
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

}