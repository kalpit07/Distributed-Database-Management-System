package queryimplementation;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static queryimplementation.ParseQuery.*;
import static queryimplementation.QueryImplementation.*;

public class ExecuteQuery {
    public static void executeCreate(String query) throws IOException {
        String[] query_parts = query.split("\\s+");
        String database = query_parts[2];

//        new File(BASE_DIRECTORY + database).mkdirs();
//        FileWriter fw_global = new FileWriter(BASE_DIRECTORY + GLOBAL_METADATA_FILE, true);
//        FileWriter fw_local = new FileWriter(BASE_DIRECTORY + LOCAL_METADATA_FILE, true);

//        fw_global.write(database + "|" + "local\n");
//        fw_local.write(database + "\n");
//
//        fw_global.close();
//        fw_local.close();
    }

    public static void executeUse(String query) {
        String[] queryParts = query.split("\\s+");
        String database = queryParts[1];

//        DATABASE = database;
    }

    public static void executeInsert() {}

    public static void executeSelect() {}

    public static void executeUpdate() {}

    public static void executeDelete() {}

//    public static boolean executeQuery(String query) {
//        String formatted_query = removeExtraSpaces(query);
//        String queryType = formatted_query.split("\\s")[0];
//
//        switch (queryType) {
//            case "create":
////                System.out.println("CREATE Query");
//                return executeCreate(formatted_query);
//            break;
//            case "use":
////                System.out.println("USE Query");
//                executeUse(formatted_query);
//                break;
//            case "insert":
////                System.out.println("INSERT Query");
//                executeInsert(formatted_query);
//                break;
//            case "select":
////                System.out.println("SELECT Query");
//                executeSelect(formatted_query);
//                break;
//            case "update":
////                System.out.println("UPDATE Query");
//                executeUpdate(formatted_query);
//                break;
//            case "delete":
////                System.out.println("DELETE Query");
//                executeDelete(formatted_query);
//                break;
//            default:
//                System.out.println("Invalid Query Type! - " + queryType.toUpperCase());
//        }
//    }
}
