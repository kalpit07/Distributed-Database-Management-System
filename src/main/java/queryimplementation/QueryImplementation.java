package queryimplementation;

import javax.xml.crypto.Data;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QueryImplementation {

    static String DATABASE;
    static String TABLE_NAME;

    static String BASE_DIRECTORY = "VM/";
    static String LOCAL_METADATA_FILE = "Local Metadata.txt";
    static String GLOBAL_METADATA_FILE = "Global Metadata.txt";

    static List<String> DATABASES = new ArrayList<>();
    static List<String> LOCAL_DATABASES = new ArrayList<>();
    static List<String> GLOBAL_DATABASES = new ArrayList<>();

    // VARCHAR LIMIT (1-255) REGEX ------------------> \b([1-9]|[1-9][0-9]|1[0-9][0-9]|2[0-5][0-5])\b

    static String REGEX_FOR_QUERY_CREATE_DATABASE = "\\s*create\\s+database\\s+[0-9a-zA-Z_]+\\s*;?\\s*";
    static String REGEX_FOR_QUERY_CREATE_TABLE = "\\s*create\\s+table\\s+[0-9a-zA-Z_]+\\s*\\(\\s*([0-9a-zA-Z_]+\\s+(int|varchar)\\s*,\\s*)*[0-9a-zA-Z_]+\\s+(int|varchar)\\s*\\)\\s*;?\\s*";
    static String REGEX_FOR_QUERY_USE = "\\s*use\\s+[0-9a-zA-Z_]+\\s*;?\\s*";
    static String REGEX_FOR_QUERY_INSERT = "\\s*insert\\s+into\\s+[0-9a-zA-Z_]+(\\s*\\(\\s*([0-9a-zA-Z_]+\\s*,\\s*)*[0-9a-zA-Z_]+\\s*\\))?\\s+values\\s*\\(\\s*(('[0-9a-zA-Z _?!@&*()-]*'|\\d+)\\s*,\\s*)*('[0-9a-zA-Z _?!@&*()-]*'|\\d+)\\s*\\)\\s*;?\\s*";
    static String REGEX_FOR_QUERY_SELECT = "\\s*select\\s+(\\*|([0-9a-zA-Z_]+\\s*,\\s*)*[0-9a-zA-Z_]+)\\s+from\\s+[0-9a-zA-Z_]+\\s*(\\swhere\\s+[0-9a-zA-Z_]+\\s*=\\s*('[0-9a-zA-Z _?!@&*()-]*'|\\d+))?;?\\s*";
    static String REGEX_FOR_QUERY_UPDATE = "\\s*update\\s+[0-9a-zA-Z_]+\\s+set\\s+(([0-9a-zA-Z_]+\\s*=\\s*('[0-9a-zA-Z _?!@&*()-]*'|\\d+)\\s*,\\s*)*[0-9a-zA-Z_]+\\s*=\\s*('[0-9a-zA-Z _?!@&*()-]*'|\\d+)\\s+where\\s+[0-9a-zA-Z_]+\\s*=\\s*('[0-9a-zA-Z _?!@&*()-]*'|\\d+)|[0-9a-zA-Z_]+\\s*=\\s*('[0-9a-zA-Z _?!@&*()-]*'|\\d+))\\s*;?\\s*";
    static String REGEX_FOR_QUERY_DELETE = "\\s*delete\\s+from\\s+[0-9a-zA-Z_]+\\s*(\\swhere\\s+[0-9a-zA-Z_]+\\s*=\\s*('[0-9a-zA-Z _?!@&*()-]*'|\\d+)\\s*)?;?\\s*";

    // ---------------------------------------------------------------------------------------------------------------

//    static String REGEX_FOR_DATABASE_NAME = "[0-9a-zA-Z$_]+";

    // Removes extra spaces at the beginning, end and in-between the words.
    // Also converts the query to lower case.
    public static String removeExtraSpaces(String query) {
        return query.trim().replaceAll("\\s+", " ").toLowerCase();
    }

    public static void getDatabase() throws FileNotFoundException {
        File global_metadata = new File(BASE_DIRECTORY + GLOBAL_METADATA_FILE);
        Scanner reader = new Scanner(global_metadata);
        while(reader.hasNextLine()) {
            String line = reader.nextLine();
            String[] line_parts = line.split("\\|");

            if (line_parts[1] == "global") {
                GLOBAL_DATABASES.add(line_parts[0]);
            }

            if (line_parts[1] == "local") {
                LOCAL_DATABASES.add(line_parts[0]);
            }

            DATABASES.add(line_parts[0]);
        }
    }

    public static void checkMetadataFile() throws IOException {
        File local_metadata = new File(BASE_DIRECTORY + LOCAL_METADATA_FILE);
        File global_metadata = new File(BASE_DIRECTORY + GLOBAL_METADATA_FILE);

        if (!local_metadata.exists()) {
            local_metadata.createNewFile();
        }

        if (!global_metadata.exists()) {
            global_metadata.createNewFile();
        }
    }

    public static void checkRootDirectory() throws IOException {
        File root_directory = new File(BASE_DIRECTORY);
        if(!root_directory.exists()) {
            root_directory.mkdirs();
        }
        checkMetadataFile();
    }

    public static boolean matchQuery(Matcher matcher, String queryType) {
        if (matcher.matches()) {
//            System.out.println("Valid " + queryType + " Query !");
            return true;
        } else {
//            System.out.println("Invalid " + queryType + " Query !");
            return false;
        }
    }

    public static boolean parseCREATEDatabase(String query) throws IOException {
        Matcher matcher = Pattern.compile(REGEX_FOR_QUERY_CREATE_DATABASE).matcher(query);
        if (matchQuery(matcher, "CREATE DATABASE")) {
          String[] query_parts = query.split("\\s+");
          String database = query_parts[2];

          for (String db : DATABASES) {
              if (db.equals(database)) {
                  System.out.println("Database already exists!");
                  return false;
              }
          }
        }

        return true;
    }

    public static boolean parseCREATETable(String query) {
        Matcher matcher = Pattern.compile(REGEX_FOR_QUERY_CREATE_TABLE).matcher(query);
        if (matchQuery(matcher, "CREATE TABLE")) {
            // Check Database, Tablename, columns
            return true;
        }
        return false;
    }

    public static boolean parseCREATE(String query) throws IOException {

        String[] query_parts = query.split("\\s+");

        if (query_parts.length >= 3) {
            String createWhat = query_parts[1];

            switch (createWhat) {
                case "database":
                    return parseCREATEDatabase(query);
                case "table":
                    return parseCREATETable(query);
                default:
                    System.out.println("Can't create - " + createWhat);
                    return false;
            }

        } else {
            System.out.println("Invalid CREATE Query!");
            return false;
        }
    }

    public static boolean parseUSE(String query) {
        Matcher matcher = Pattern.compile(REGEX_FOR_QUERY_USE).matcher(query);
        if (matchQuery(matcher, "USE")) {
            String[] query_parts = query.split("\\s+");
            String database = query_parts[1];

            for (String db : DATABASES) {
                if (db.equals(database)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean parseINSERT(String query) {
        Matcher matcher = Pattern.compile(REGEX_FOR_QUERY_INSERT).matcher(query);
        return matchQuery(matcher, "INSERT");
    }

    public static boolean parseSELECT(String query) {
        Matcher matcher = Pattern.compile(REGEX_FOR_QUERY_SELECT).matcher(query);
        return matchQuery(matcher, "SELECT");
    }

    public static boolean parseUPDATE(String query) {
        Matcher matcher = Pattern.compile(REGEX_FOR_QUERY_UPDATE).matcher(query);
        return matchQuery(matcher, "UPDATE");
    }

    public static boolean parseDELETE(String query) {
        Matcher matcher = Pattern.compile(REGEX_FOR_QUERY_DELETE).matcher(query);
        return matchQuery(matcher, "DELETE");
    }

    public static void executeCREATE(String query) throws IOException {
        String[] query_parts = query.split("\\s+");
        String database = query_parts[2];

        new File(BASE_DIRECTORY + database).mkdirs();
        FileWriter fw_global = new FileWriter(BASE_DIRECTORY + GLOBAL_METADATA_FILE, true);
        FileWriter fw_local = new FileWriter(BASE_DIRECTORY + LOCAL_METADATA_FILE, true);

        fw_global.write(database + "|" + "local\n");
        fw_local.write(database + "\n");

        fw_global.close();
        fw_local.close();
    }

    public static void executeUSE(String query) {
        String[] queryParts = query.split("\\s+");
        String database = queryParts[1];

        DATABASE = database;
    }

    public static void executeINSERT() {}

    public static void executeSELECT() {}

    public static void executeUPDATE() {}

    public static void executeDELETE() {}

    public static boolean parseQuery(String query) throws IOException {

        String formatted_query = removeExtraSpaces(query);
        String queryType = formatted_query.split("\\s")[0];

        if (formatted_query.substring(formatted_query.length()-1).equals(";")) {
            formatted_query = formatted_query.substring(0, formatted_query.length()-1).trim();
        }


        switch (queryType) {
            case "create":
                System.out.println("CREATE Query");
                return parseCREATE(formatted_query);
            case "use":
                System.out.println("USE Query");
                return parseUSE(formatted_query);
            case "insert":
                System.out.println("INSERT Query");
                return parseINSERT(formatted_query);
            case "select":
                System.out.println("SELECT Query");
                return parseSELECT(formatted_query);
            case "update":
                System.out.println("UPDATE Query");
                return parseUPDATE(formatted_query);
            case "delete":
                System.out.println("DELETE Query");
                return parseDELETE(formatted_query);
            default:
                System.out.println("Invalid Query Type! - " + queryType.toUpperCase());
                return false;
        }

    }

//    public static boolean executeQuery(String query) {
//        String formatted_query = removeExtraSpaces(query);
//        String queryType = formatted_query.split("\\s")[0];
//
//        switch (queryType) {
//            case "create":
////                System.out.println("CREATE Query");
//                return executeCREATE(formatted_query);
//            break;
//            case "use":
////                System.out.println("USE Query");
//                executeUSE(formatted_query);
//                break;
//            case "insert":
////                System.out.println("INSERT Query");
//                executeINSERT(formatted_query);
//                break;
//            case "select":
////                System.out.println("SELECT Query");
//                executeSELECT(formatted_query);
//                break;
//            case "update":
////                System.out.println("UPDATE Query");
//                executeUPDATE(formatted_query);
//                break;
//            case "delete":
////                System.out.println("DELETE Query");
//                executeDELETE(formatted_query);
//                break;
//            default:
//                System.out.println("Invalid Query Type! - " + queryType.toUpperCase());
//        }
//    }

    public static void main(String[] args) throws IOException {
        checkRootDirectory();
        getDatabase();
//        String query = "USE teachers;";
//        System.out.println("before + " + DATABASE);
        System.out.println(parseQuery("USE a1;"));
//        System.out.println("after + " + DATABASE);

//        System.out.println("DATABASE : " + DATABASE);
//        parseQuery("Use students;");
//        System.out.println("DATABASE : " + DATABASE);
//        parseQuery("USE teachers;");
//        System.out.println("DATABASE : " + DATABASE);
//        parseQuery("USE avengers;");
//        System.out.println("DATABASE : " + DATABASE);
    }
}
