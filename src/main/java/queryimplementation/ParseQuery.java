package queryimplementation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static queryimplementation.ExecuteQuery.*;
import static queryimplementation.QueryImplementation.*;

public class ParseQuery {

    // VARCHAR LIMIT (1-255) REGEX ------------------> \(([1-9]|[1-9][0-9]|[1-4][0-9][0-9]|500)\)

    // create database Info;
    public static String REGEX_FOR_QUERY_CREATE_DATABASE = "\\s*create\\s+database\\s+[0-9a-zA-Z_]+\\s*;\\s*";

    // create table Orders(OrderID int,OrderNumber int,PersonID int,primary key (OrderID),foreign key (PersonID) references Persons(PersonID));
    public static String REGEX_FOR_QUERY_CREATE_TABLE = "\\s*create\\s+table\\s+[0-9a-zA-Z_]+\\s*\\(\\s*([0-9a-zA-Z_]+\\s+(int|float|boolean|varchar\\(([1-9]|[1-9][0-9]|[1-4][0-9][0-9]|500)\\))\\s*,\\s*)*[0-9a-zA-Z_]+\\s+(int|float|boolean|varchar\\(([1-9]|[1-9][0-9]|[1-4][0-9][0-9]|500)\\))(\\s*,\\s*primary\\s+key\\s*\\(\\s*[0-9a-zA-Z_]+\\s*\\))?(\\s*,\\s*foreign\\s+key\\s*\\(\\s*[0-9a-zA-Z_]+\\s*\\)\\s+references\\s+[0-9a-zA-Z_]+\\s*\\(\\s*[0-9a-zA-Z_]+\\s*\\))*\\s*\\)\\s*;\\s*";

    // use Info;
    public static String REGEX_FOR_QUERY_USE = "\\s*use\\s+[0-9a-zA-Z_]+\\s*;\\s*";

    // insert into Students values ('kalpit', 51);
    // insert into Students (name, id) values ('kalpit', 51);
    public static String REGEX_FOR_QUERY_INSERT = "\\s*insert\\s+into\\s+[0-9a-zA-Z_]+(\\s*\\(\\s*([0-9a-zA-Z_]+\\s*,\\s*)*[0-9a-zA-Z_]+\\s*\\))?\\s+values\\s*\\(\\s*(('[0-9a-zA-Z _?!@&*()-]*'|\\d+)\\s*,\\s*)*('[0-9a-zA-Z _?!@&*()-]*'|\\d+)\\s*\\)\\s*;\\s*";

    // select * from Students;
    // select * from Students where id=51;
    // select column1, column2 from students where id=5;
    public static String REGEX_FOR_QUERY_SELECT = "\\s*select\\s+(\\*|([0-9a-zA-Z_]+\\s*,\\s*)*[0-9a-zA-Z_]+)\\s+from\\s+[0-9a-zA-Z_]+\\s*(\\swhere\\s+[0-9a-zA-Z_]+\\s*=\\s*('[0-9a-zA-Z _?!@&*()-]*'|\\d+))?;\\s*";

    // update Students set column1='v1', column2=96 where id='a2c';
    public static String REGEX_FOR_QUERY_UPDATE = "\\s*update\\s+[0-9a-zA-Z_]+\\s+set\\s+([0-9a-zA-Z_]+\\s*=\\s*('[0-9a-zA-Z _?!@&*()-]*'|\\d+)\\s*,\\s*)*[0-9a-zA-Z_]+\\s*=\\s*('[0-9a-zA-Z _?!@&*()-]*'|\\d+)\\s+where\\s+[0-9a-zA-Z_]+\\s*=\\s*('[0-9a-zA-Z _?!@&*()-]*'|\\d+)\\s*;\\s*";

    // delete from Students where ID = 51;
    public static String REGEX_FOR_QUERY_DELETE = "\\s*delete\\s+from\\s+[0-9a-zA-Z_]+\\s+where\\s+[0-9a-zA-Z_]+\\s*=\\s*('[0-9a-zA-Z _?!@&*()-]*'|\\d+)\\s*;\\s*";



    public static String REGEX_FOR_TRANSACTION = "\\s*start\\s+transaction\\s*;\\s*";

    // ---------------------------------------------------------------------------------------------------------------

    public static String BASE_DIRECTORY = "VM/";
    public static String LOCAL_METADATA_FILE = "Global_Data_Dictionary.txt";
    public static String GLOBAL_METADATA_FILE = "Local_Metadata.txt";

    public static List<String> DATABASES = new ArrayList<>();
    public static List<String> LOCAL_DATABASES = new ArrayList<>();
    public static List<String> GLOBAL_DATABASES = new ArrayList<>();

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

    public static String formatQuery(String query) {

        return query.substring(0, query.length()-1).trim().replaceAll("\\s+", " ");
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

    public static boolean createDatabase(String query) throws IOException {
        Matcher matcher = Pattern.compile(REGEX_FOR_QUERY_CREATE_DATABASE).matcher(query);
        if (matchQuery(matcher, "CREATE DATABASE")) {
            query = formatQuery(query.trim());
            String[] query_parts = query.split("\\s+");
            String database = query_parts[2];

            for (String db : DATABASES) {
                if (db.equals(database)) {
                    System.out.println("Database already exists!");
                    return false;
                }
            }
            return true;
        }

        return false;
    }

    public static boolean createTable(String query) throws FileNotFoundException {
        Matcher matcher = Pattern.compile(REGEX_FOR_QUERY_CREATE_TABLE).matcher(query);
        if (matchQuery(matcher, "CREATE TABLE")) {
            if (DATABASE != null) {
                query = formatQuery(query.trim());

                int firstParenthesis=0, lastParenthesis=0;
                for (int index=0; index<query.length(); index++) {
                    if (Character.compare(query.toCharArray()[index], '(') == 0) {
                        firstParenthesis = index;
                        break;
                    }
                }
                for (int index=query.length()-1; index>=0; index--) {
                    if (Character.compare(query.toCharArray()[index], ')') == 0) {
                        lastParenthesis = index;
                        break;
                    }
                }

                TABLE_NAME = query.substring(0, firstParenthesis).trim().split("\\s+")[2];

                // Check if table exists or not
                String metadata_file_path = BASE_DIRECTORY + LOCAL_METADATA_FILE;
                boolean table_found = false, primary_key=true, foreign_key=true, different_column_names = false;

                File local_metadata = new File(metadata_file_path);
                Scanner reader = new Scanner(local_metadata);

                // Checking for table_name
                while(reader.hasNextLine()) {
                    String line = reader.nextLine();
                    String[] line_parts = line.split("\\|");

                    if (line_parts[0].equals(DATABASE)) {
                        String[] table_parts = line_parts[1].trim().split(",");
                        for (String table : table_parts) {
                            if (table.equals(TABLE_NAME)) {
                                table_found = true;
                                break;
                            }
                        }
                    }
                }

                // Checking all column names are different
                Set<String> column_names = new HashSet<String>();
                int column_count = 0;

                String[] column_parts = query.substring(firstParenthesis+1,lastParenthesis).trim().split(",");
                for (String s : column_parts) {
                    if (!(s.contains("primary key") || s.contains("foreign key"))) {
                        column_count += 1;

                        String[] s_parts = s.trim().split("\\s+");
                        column_names.add(s_parts[0]);
                    }
                }
                if (column_names.size() == column_count) {
                    different_column_names = true;
                }


                // Checking for primary key
                if (query.contains("primary key")) {
                    primary_key = false;

                    int first=0, last=0;
                    int start_index = query.indexOf("primary key") + "primary key".length();
                    for (int i=start_index; i<query.length(); i++) {
                        if (Character.compare(query.toCharArray()[i], '(') == 0) {
                            first = i;
                        }
                        if (Character.compare(query.toCharArray()[i], ')') == 0) {
                            last = i;
                            break;
                        }
                    }

                    String pk_column = query.substring(first+1, last).trim();

                    for (String column : column_names) {
                        if (column.equals(pk_column)) {
                            primary_key = true;
                        }
                    }

                }

                // Checking for foreign key
                if (query.contains("foreign key")) {
                    foreign_key = false;
                }


                if (!table_found && primary_key && foreign_key && different_column_names) {
                    return true;
                }
                return false;
            }
            System.out.println("No database selected!");
            return false;
        }
        return false;
    }

    public static boolean parseCreate(String query) throws IOException {

        String[] query_parts = query.split("\\s+");

        if (query_parts.length >= 3) {
            String createWhat = query_parts[1];

            switch (createWhat) {
                case "database":
                    return createDatabase(query);
                case "table":
                    return createTable(query);
                default:
                    System.out.println("Can't create - " + createWhat);
                    return false;
            }

        } else {
            System.out.println("Invalid CREATE Query!");
            return false;
        }
    }

    public static boolean parseUse(String query) {
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

    public static boolean parseInsert(String query) {
        Matcher matcher = Pattern.compile(REGEX_FOR_QUERY_INSERT).matcher(query);
        return matchQuery(matcher, "INSERT");
    }

    public static boolean parseSelect(String query) {
        Matcher matcher = Pattern.compile(REGEX_FOR_QUERY_SELECT).matcher(query);
        return matchQuery(matcher, "SELECT");
    }

    public static boolean parseUpdate(String query) {
        Matcher matcher = Pattern.compile(REGEX_FOR_QUERY_UPDATE).matcher(query);
        return matchQuery(matcher, "UPDATE");
    }

    public static boolean parseDelete(String query) {
        Matcher matcher = Pattern.compile(REGEX_FOR_QUERY_DELETE).matcher(query);
        return matchQuery(matcher, "DELETE");
    }

    public static boolean parseQuery(String query) throws IOException {

        String queryType = query.split("\\s+")[0];

        switch (queryType) {
            case "create":
                System.out.println("CREATE Query");
                return parseCreate(query);
            case "use":
                System.out.println("USE Query");
                return parseUse(query);
            case "insert":
                System.out.println("INSERT Query");
                return parseInsert(query);
            case "select":
                System.out.println("SELECT Query");
                return parseSelect(query);
            case "update":
                System.out.println("UPDATE Query");
                return parseUpdate(query);
            case "delete":
                System.out.println("DELETE Query");
                return parseDelete(query);
            case "start":
                // Transaction
                return true;
            default:
                System.out.println("Invalid Query Type! - " + queryType.toUpperCase());
                return false;
        }

    }
}
