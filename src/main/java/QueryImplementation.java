import CreateOperation.CreateDatabase;
import CreateOperation.CreateTable;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QueryImplementation {

    static String DATABASE;
    static String TABLE_NAME;

    // VARCHAR LIMIT (1-255) REGEX ------------------> \b([1-9]|[1-9][0-9]|1[0-9][0-9]|2[0-5][0-5])\b

    static String REGEX_FOR_QUERY_CREATE_DATABASE = "\\s*create\\s+database\\s+[0-9a-zA-Z_]+\\s*;?\\s*";
    static String REGEX_FOR_QUERY_CREATE_TABLE = "\\s*create\\s+table\\s+[0-9a-zA-Z_]+\\s*\\(\\s*([0-9a-zA-Z_]+\\s+(int|varchar)\\s*,\\s*)*[0-9a-zA-Z_]+\\s+(int|varchar)\\s*\\)\\s*;?\\s*";
    static String REGEX_FOR_QUERY_USE = "\\s*use//s+[0-9a-zA-Z_]+\\s*;?\\s*";
    static String REGEX_FOR_QUERY_INSERT = "\\s*insert\\s+into\\s+[0-9a-zA-Z_]+(\\s*\\(\\s*([0-9a-zA-Z_]+\\s*,\\s*)*[0-9a-zA-Z_]+\\s*\\))?\\s+values\\s*\\(\\s*(('[0-9a-zA-Z _?!@&*()-]*'|\\d+)\\s*,\\s*)*('[0-9a-zA-Z _?!@&*()-]*'|\\d+)\\s*\\)\\s*;?\\s*";
    static String REGEX_FOR_QUERY_SELECT = "\\s*select\\s+(\\*|([0-9a-zA-Z_]+\\s*,\\s*)*[0-9a-zA-Z_]+)\\s+from\\s+[0-9a-zA-Z_]+\\s*(\\swhere\\s+[0-9a-zA-Z_]+\\s*=\\s*('[0-9a-zA-Z _?!@&*()-]*'|\\d+))?;?\\s*";
    static String REGEX_FOR_QUERY_UPDATE = "\\s*update\\s+[0-9a-zA-Z_]+\\s+set\\s+(([0-9a-zA-Z_]+\\s*=\\s*('[0-9a-zA-Z _?!@&*()-]*'|\\d+)\\s*,\\s*)*[0-9a-zA-Z_]+\\s*=\\s*('[0-9a-zA-Z _?!@&*()-]*'|\\d+)\\s+where\\s+[0-9a-zA-Z_]+\\s*=\\s*('[0-9a-zA-Z _?!@&*()-]*'|\\d+)|[0-9a-zA-Z_]+\\s*=\\s*('[0-9a-zA-Z _?!@&*()-]*'|\\d+))\\s*;?\\s*";
    static String REGEX_FOR_QUERY_DELETE = "\\s*delete\\s+from\\s+[0-9a-zA-Z_]+\\s*(\\swhere\\s+[0-9a-zA-Z_]+\\s*=\\s*('[0-9a-zA-Z _?!@&*()-]*'|\\d+)\\s*)?;?\\s*";

    // ---------------------------------------------------------------------------------------------------------------

//    static String REGEX_FOR_DATABASE_NAME = "[0-9a-zA-Z$_]+";


    public static void matchQuery(Matcher matcher, String queryType) {
        if (matcher.matches()) {
            System.out.println("Valid " + queryType + " Query !");
        } else {
            System.out.println("Invalid " + queryType + " Query !");
        }
    }

    public static void createDatabase(String query) {
        Matcher matcher = Pattern.compile(REGEX_FOR_QUERY_CREATE_DATABASE).matcher(query);
        matchQuery(matcher, "CREATE DATABASE");
        CreateDatabase database = new CreateDatabase(query);
        database.executeQuery();
        // **************** CREATE folder for new database
        System.out.println("Database \"" + "db_name" + "\" created successfully.");
        // **************** Add Database to Local META file
        // **************** Add Database to Global META file
    }

    public static void createTable(String query) {
        Matcher matcher = Pattern.compile(REGEX_FOR_QUERY_CREATE_TABLE).matcher(query);
        matchQuery(matcher, "CREATE TABLE");
        // VM1 or VM2 , also pass database name
        //we need database path and database name here from use database class from setters and getters
        //usedb.getdbpath(), usedb.getdbname()
        CreateTable createTable = new CreateTable("VM1/persons");
        createTable.identifyTheCreateQueryElements(query);
        createTable.createAFileForTypeOfAttributesOfTheTable();
        createTable.createAFileForValuesOfTheTable();
    }

    public static void useDatabase() {
        // **************** Check if database exists in Global META file
        // **************** Add Database to Global META file
        // set the database name
    }

    public static void parseCREATE(String query) {

        String[] query_parts = query.split("\\s+");

        if (query_parts.length >= 3) {
            String createWhat = query_parts[1];

            switch (createWhat) {
                case "database":
                    createDatabase(query);
                    break;
                case "table":
                    createTable(query);
                    break;
                default:
                    System.out.println("Can't create - " + createWhat);
            }

        } else {
            System.out.println("Invalid CREATE Query!");
        }
    }

    public static void parseUSE(String query) {
        Matcher matcher = Pattern.compile(REGEX_FOR_QUERY_USE).matcher(query);
        matchQuery(matcher, "USE");
    }

    public static void parseINSERT(String query) {
        Matcher matcher = Pattern.compile(REGEX_FOR_QUERY_INSERT).matcher(query);
        matchQuery(matcher, "INSERT");
    }

    public static void parseSELECT(String query) {
        Matcher matcher = Pattern.compile(REGEX_FOR_QUERY_SELECT).matcher(query);
        matchQuery(matcher, "SELECT");
    }

    public static void parseUPDATE(String query) {
        Matcher matcher = Pattern.compile(REGEX_FOR_QUERY_UPDATE).matcher(query);
        matchQuery(matcher, "UPDATE");
    }

    public static void parseDELETE(String query) {
        Matcher matcher = Pattern.compile(REGEX_FOR_QUERY_DELETE).matcher(query);
        matchQuery(matcher, "DELETE");
    }

    // Removes extra spaces at the beginning, end and in-between the words.
    // Also converts the query to lower case.
    public static String removeExtraSpaces(String query) {
        return query.trim().replaceAll("\\s+", " ").toLowerCase();
    }

    public static void parseQuery(String query) {

        String formatted_query = removeExtraSpaces(query);
        String queryType = formatted_query.split("\\s")[0];

//        Remove ';' at the end
//        query = query.substring(0, query.length()-1).trim();

        switch (queryType) {
            case "create":
                System.out.println("CREATE Query");
                parseCREATE(formatted_query);
                break;
            case "use":
                System.out.println("USE Query");
                parseUSE(formatted_query);
                break;
            case "insert":
                System.out.println("INSERT Query");
                parseINSERT(formatted_query);
                break;
            case "select":
                System.out.println("SELECT Query");
                parseSELECT(formatted_query);
                break;
            case "update":
                System.out.println("UPDATE Query");
                parseUPDATE(formatted_query);
                break;
            case "delete":
                System.out.println("DELETE Query");
                parseDELETE(formatted_query);
                break;
            default:
                System.out.println("Invalid Query Type! - " + queryType.toUpperCase());
        }

    }

    public static void main(String[] args) {
        String query = "UPDATE Customers SET ContactName = 'Ken Miles', City= 'Frankfurt' WHERE CustomerID = 364;";
        parseQuery(query);
    }
}
