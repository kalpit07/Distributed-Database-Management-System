package queryimplementation;

import javax.xml.crypto.Data;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static queryimplementation.ParseQuery.*;
import static queryimplementation.ExecuteQuery.*;

public class QueryImplementation {

    public static String DATABASE;
    public static String TABLE_NAME;

    public static String BASE_DIRECTORY = "VM/";
    public static String LOCAL_METADATA_FILE = "Local Metadata.txt";
    public static String GLOBAL_METADATA_FILE = "Global Metadata.txt";

    public static List<String> DATABASES = new ArrayList<>();
    public static List<String> LOCAL_DATABASES = new ArrayList<>();
    public static List<String> GLOBAL_DATABASES = new ArrayList<>();



//    static String REGEX_FOR_DATABASE_NAME = "[0-9a-zA-Z$_]+";

    // Removes extra spaces at the beginning, end and in-between the words.
    // Also converts the query to lower case.
    public static String removeExtraSpaces(String query) {
        return query.trim().replaceAll("\\s+", " ").toLowerCase();
    }













    public static void main(String[] args) throws IOException {
//        checkRootDirectory();
//        getDatabase();

        if (parseQuery(query)) {

        }
        executeQuery(query);

//        String query = "USE teachers;";
//        System.out.println("before + " + DATABASE);

//        System.out.println(formatQuery("create database    sdasda   ;"));

//        String primary_key_string = Pattern.compile().matcher("create table Orders(OrderID int,OrderNumber int,PersonID int,primary key (OrderID),foreign key (PersonID) references Persons(PersonID));").group(1);

//        Pattern pattern = Pattern.compile("primary\\skey\\s*\\(\\s*[0-9a-zA-Z_]+\\s*\\)\\s*");
//        Matcher matcher = pattern.matcher("create table Orders(OrderID int,OrderNumber int,PersonID int,primary key (OrderID),foreign key (PersonID) references Persons(PersonID));");

//        Pattern pattern = Pattern.compile("[0-9a-zA-Z_]+");
//        Matcher matcher = pattern.matcher("create table Orders(OrderID int,OrderNumber int,PersonID int,primary key (OrderID),foreign key (PersonID) references Persons(PersonID));");
//
//
//        if (matcher.find()) {
//            System.out.println("Hi");
//            System.out.println(matcher.group(1));
//            System.out.println(matcher.group(2));
////            Assert.assertEquals("25-09-1984", matcher.group());
//        }

        String query = "create table Orders(OrderID int,OrderNumber int,PersonID int,primary key (OrderID),foreign key (PersonID) references Persons(PersonID));";
//        int first=0, last=0;
//        int start_index = query.indexOf("primary key") + "primary key".length();
//        for (int i=start_index; i<query.length(); i++) {
//            if (Character.compare(query.toCharArray()[i], '(') == 0) {
//                first = i;
//            }
//            if (Character.compare(query.toCharArray()[i], ')') == 0) {
//                last = i;
//                break;
//            }
//        }
//
//        String pk_column = query.substring(first+1, last).trim();
//        System.out.println(pk_column);

        Set<String> column_names = new HashSet<String>();
        int column_count = 0;

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

        String[] column_parts = query.substring(firstParenthesis+1,lastParenthesis).trim().split(",");
        for (String s : column_parts) {
            if (!(s.contains("primary key") || s.contains("foreign key"))) {
                column_count += 1;

                String[] s_parts = s.trim().split("\\s+");
                column_names.add(s_parts[0]);
            }
        }
        System.out.println(column_count);
        System.out.println(column_names);

    }
}
