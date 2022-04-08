package queryimplementation;

import datadump.DataDumpHandler;

import java.io.IOException;
import java.util.*;

import static queryimplementation.ParseQuery.*;
import static queryimplementation.ExecuteQuery.*;

public class QueryImplementation {

    public static String DATABASE;
    public static String TABLE_NAME;

    public static String VM_INSTANCE = "VM1";

    public static String BASE_DIRECTORY = "VM/";
    public static String LOCAL_METADATA_FILE = "Local_Meta_Data.txt";
    public static String GLOBAL_METADATA_FILE = "Global_Data_Dictionary.txt";

    public static List<String> DATABASES = new ArrayList<>();
    public static List<String> LOCAL_DATABASES = new ArrayList<>();
    public static List<String> GLOBAL_DATABASES = new ArrayList<>();


    public static void main(String[] args) throws IOException {


        String[] queries = {
                "CREATE DATABASE students;",
                "CREATE DATABASE teachers;",

                "USE students;",

                "CREATE TABLE student_details (ID int, Name varchar(25), phone varchar(10), PRIMARY KEY (ID), FOREIGN KEY (name) REFERENCES teachers(name));",
                
                "INSERT INTO student_details VALUES (1, 'kalpit', '1111111111');",
                "INSERT INTO student_details VALUES (2, 'vishnu', '2222222222');",
                "INSERT INTO student_details VALUES (3, 'kavya', '3333333333');",
                "INSERT INTO student_details VALUES (4, 'sharad', '4444444444');",
                "INSERT INTO student_details VALUES (5, 'kishan', '5555555555');",

                "CREATE TABLE student_grades (ID int, sub1 float, sub2 float);",

                "INSERT INTO student_grades VALUES (1, 85, 92);",
                "INSERT INTO student_grades VALUES (2, 87, 92);",
                "INSERT INTO student_grades VALUES (3, 80, 99);",
                "INSERT INTO student_grades VALUES (4, 76, 91);",
                "INSERT INTO student_grades VALUES (5, 100, 82);",

                "SELECT * FROM student_grades where sub2=92;",
                "SELECT name, phone FROM student_details;",

                "USE teachers;",

                "CREATE TABLE staff (t_id int, t_name varchar(25));"
        };

        for (String q : queries) {
            if(parseQuery(q)) {
                executeQuery(q);
                DataDumpHandler ddh=new DataDumpHandler();
                ddh.exportDump("students");
            } else {
                System.out.println("INVALID QUERY!");
            }
        }


    }
}
