package queryimplementation;

import datadump.DataDumpHandler;

import java.io.IOException;
import java.util.*;

import static queryimplementation.ParseQuery.*;
import static queryimplementation.ExecuteQuery.*;
import static logmanagement.LogManagement.*;

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


    public static void main(String[] args) throws Exception {

        ParseQuery pq = new ParseQuery();
        ExecuteQuery eq = new ExecuteQuery();

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


//        for (String query : queries) {
//            if (pq.parseQuery(query)) {
//                eq.executeQuery(query);
//            } else {
//                System.out.println("INVALID QUERY!");
//            }
//        }
//        System.out.println(parseQuery("SELECT name, id from student_detail;"));

//        String[] qs = {"USE students;", "INSERT into student_details values (2, 'vishnu', '8273748273');"};
//        for (String q : qs) {
//            if (pq.parseQuery(q)) {
//                eq.executeQuery(q);
//            }
//        }

        String[] qs = {
                "CREATE DATABASE students;",
                "CREATE DATABASE teachers;",

                "USE teachers;",

                "CREATE TABLE teacher_details (teacher_ID int, Name varchar(25), phone varchar(10), PRIMARY KEY (teacher_ID));",

                "INSERT INTO teacher_details VALUES (1, 'kalpit', '1111111111');",
                "INSERT INTO teacher_details VALUES (2, 'vishnu', '2222222222');",
                "INSERT INTO teacher_details VALUES (3, 'kavya', '3333333333');",
                "INSERT INTO teacher_details VALUES (4, 'sharad', '4444444444');",
                "INSERT INTO teacher_details VALUES (5, 'kishan', '5555555555');",

                "CREATE TABLE students_info (student_ID int, t_ID int, sub1 int, sub2 int, PRIMARY KEY (student_ID), FOREIGN KEY (t_ID) REFERENCES teacher_details(teacher_ID));",

                "INSERT INTO students_info VALUES (91, 2, 85, 92);",
                "INSERT INTO students_info VALUES (92, 3, 87, 92);",
                "INSERT INTO students_info VALUES (93, 2, 80, 99);",
                "INSERT INTO students_info VALUES (94, 4, 76, 91);",
                "INSERT INTO students_info VALUES (95, 6, 100, 82);",

                "SELECT * FROM students_info where sub2=92;",
                "SELECT teacher_id, name, phone FROM teacher_details;",

                "USE students;",

                "CREATE TABLE hehe (t_id int, t_name varchar(25));"
        };

//        for (String s : qs) {
//            if (pq.parseQuery(s)) {
//                eq.executeQuery(s);
//            } else {
//                System.out.println("INVALID QUERY!");
//            }
//        }


//        String[] a = {"USE teachers;", "select * from teacher_details where name='kalpit';"};
//
//        for (String s : a) {
//            if (pq.parseQuery(s)) {
//                eq.executeQuery(s);
//            }
//        }

//        pq.parseQuery("use teachers;");
        eq.executeQuery("use teachers;");
        pq.parseQuery("CREATE TABLE abc123 (id int, name varchar(255), primary key (id), foreign key (       name         ) references students_info  (          sub1 )        )           ;");

    }
}
