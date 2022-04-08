package queryimplementation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import static queryimplementation.ParseQuery.*;
import static queryimplementation.QueryImplementation.*;

public class ExecuteQuery {

    public static void executeCreateDatabase(String query) throws IOException {
        String[] query_parts = query.split("\\s+");
        String database = query_parts[2];

        new File(BASE_DIRECTORY + database).mkdirs();

        String meta_data_file_name = BASE_DIRECTORY + database + "/" + database + "_metadata.txt";
        new File(meta_data_file_name).createNewFile();

        FileWriter fw_global = new FileWriter(BASE_DIRECTORY + GLOBAL_METADATA_FILE, true);
        FileWriter fw_local = new FileWriter(BASE_DIRECTORY + LOCAL_METADATA_FILE, true);

        fw_global.write(database + "|" + VM_INSTANCE + "\n");
        fw_local.write(database + "|\n");

        fw_global.close();
        fw_local.close();
    }

    public static void executeCreateTable(String query) throws IOException {

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


        // Getting column names and data types
        List<String> column_names = new ArrayList<>();
        List<String> column_types = new ArrayList<>();
        String primary_key=null;
        String foreign_key=null, fk_details=null;


        String[] column_parts = query.substring(firstParenthesis+1,lastParenthesis).trim().split(",");
        for (String s : column_parts) {
            if (!(s.contains("primary key") || s.contains("foreign key"))) {

                String[] s_parts = s.trim().split("\\s+");
                column_names.add(s_parts[0]);
                column_types.add(s_parts[1]);
            } else if (s.contains("primary key")) {
                primary_key = s.substring(s.indexOf("(") + "(".length());
                primary_key = primary_key.substring(0, primary_key.indexOf(")")).trim();
            } else if (s.contains("foreign key")) {
                fk_details = "FOREIGN_KEY ";

                foreign_key = s.substring(s.indexOf("(") + "(".length());
                foreign_key = foreign_key.substring(0, foreign_key.indexOf(")")).trim();

                fk_details += s.substring(s.indexOf("references"));


            }
        }

        String metadata_header = "";
        String header = "";
        for (int i=0; i<column_names.size(); i++) {
            header += column_names.get(i) + "|";

            if (column_names.get(i).equals(primary_key)) {
                metadata_header += column_names.get(i) + " " + column_types.get(i) + " PRIMARY_KEY,";
            } else if (column_names.get(i).equals(foreign_key)) {
                metadata_header += column_names.get(i) + " " + column_types.get(i) + " " + fk_details + ",";
            } else {
                metadata_header += column_names.get(i) + " " + column_types.get(i) + ",";
            }
        }
        metadata_header = metadata_header.substring(0, metadata_header.length()-1);
        header = header.substring(0, header.length()-1) + "\n";


        // add to local metadata files
        String meta_data_file_name = BASE_DIRECTORY + DATABASE + "/" + DATABASE + "_metadata.txt";
        FileWriter fw_local = new FileWriter(meta_data_file_name, true);
        fw_local.write(TABLE_NAME + "|" + metadata_header + "\n");
        fw_local.close();


        // add table name to Local_Meta_Data.txt file
        List<String> data = new ArrayList<String>();
        String file_path = BASE_DIRECTORY + LOCAL_METADATA_FILE;

        File metadata = new File(file_path);
        Scanner reader = new Scanner(metadata);
        while(reader.hasNextLine()) {
            String line = reader.nextLine();
            String[] line_parts = line.split("\\|");

            if (line_parts[0].equals(DATABASE)) {
                if (line_parts.length > 1) {
                    line = line.substring(0, line.length()-1);
                    line += "," + TABLE_NAME + "\n";
                } else {
                    line = DATABASE + "|" + TABLE_NAME + "\n";
                }
            } else {
                line += "\n";
            }
            data.add(line);
        }

        FileWriter md = new FileWriter(file_path, false);
        for (String d : data) {
            md.write(d);
        }
        md.close();


        // create table file
        String table_filepath = BASE_DIRECTORY + DATABASE + "/" + TABLE_NAME + ".txt";
        File table = new File(table_filepath);
        table.createNewFile();
        FileWriter t = new FileWriter(table_filepath);
        t.write(header);
        t.close();
    }

    public static void executeCreate(String query) throws IOException {

        String[] query_parts = query.split("\\s+");
        String createWhat = query_parts[1];

        if (createWhat.equals("database")) {
            executeCreateDatabase(query);
        } else if (createWhat.equals("table")) {
            executeCreateTable(query);
        } else {
            System.out.println("Invalid!");
        }
    }

    public static void executeUse(String query) {
        String[] queryParts = query.split("\\s+");
        String database = queryParts[1];

        DATABASE = database;
    }

    public static void executeInsert(String query) throws IOException {
        // get table_name
        String[] query_parts = query.split("\\s+");
        TABLE_NAME = query_parts[2];

        String file_path = BASE_DIRECTORY + DATABASE + "/" + TABLE_NAME + ".txt";

        String value_str = query.substring(query.indexOf("values") + "values".length()).trim();
        value_str = value_str.substring(1, value_str.length()-1).trim();

        String[] values = value_str.split(",");
        String data = "";

        for (String value : values) {
            value = value.trim();
            if (value.contains("'")) {
                value = value.substring(1,value.length()-1);
            }

            data += value + "|";
        }
        data = data.substring(0, data.length()-1).trim() + "\n";

        FileWriter fw = new FileWriter(file_path, true);
        fw.write(data);
        fw.close();


    }

    public static void executeSelect(String query) throws FileNotFoundException {

        String[] query_parts = query.trim().split("\\s+");

        List<String> data = new ArrayList<String>();
        List<String> column_names = new ArrayList<String>();
        String q = query;

        // get table name
        if (query.contains("where")) {
            q = q.substring(0, q.indexOf("where")-1);
            int size = q.split("\\s+").length;
            TABLE_NAME = q.split("\\s+")[q.split("\\s+").length-1];
        } else {
            int size = q.split("\\s+").length;
            TABLE_NAME = q.split("\\s+")[q.split("\\s+").length-1];
        }

        String file_path = BASE_DIRECTORY + DATABASE + "/" + TABLE_NAME + ".txt";

        // get column names
        String header=null;
        File f = new File(file_path);
        Scanner sc = new Scanner(f);
        while (sc.hasNextLine()) {
            header = sc.nextLine();
            break;
        }
        String[] column_n = header.split("\\|");
        for (String c : column_n) {
            column_names.add(c);
        }

        // Check for * or column names
        if (query_parts[1].equals("*")) {
            if (query.contains("where")) {
                String column_name = query.substring(query.indexOf("where ") + "where ".length());
                column_name = column_name.substring(0, column_name.indexOf("=")).trim();

                String value = query.substring(query.indexOf("=") + "=".length()).trim();

                int column_number=0;
                for (int j=0; j<column_names.size(); j++) {
                    if (column_names.get(j).equals(column_name)) {
                        column_number = j;
                        break;
                    }
                }

                data.add(header);
                File file = new File(file_path);
                Scanner scanner = new Scanner(file);
                while (scanner.hasNextLine()) {
                    String d = scanner.nextLine();
                    String[] d_parts = d.split("\\|");
                    if (d_parts[column_number].equals(value)) {
                        data.add(d);
                    }
                }
                scanner.close();

            } else {
                // where is not included
                // All data
                File file = new File(file_path);
                Scanner reader = new Scanner(file);
                while (reader.hasNextLine()) {
                    String d = reader.nextLine();
                    data.add(d);
                }
                reader.close();
            }
        } else {
            // Column names

            if (query.contains("where")) {
                String column_name = query.substring(query.indexOf("where ") + "where ".length());
                column_name = column_name.substring(0, column_name.indexOf("=")).trim();

                String value = query.substring(query.indexOf("=") + "=".length()).trim();

                // get require column names and number
                List<Integer> column_numbers = new ArrayList<Integer>();
                String c_names = query.substring(query.indexOf("select ") + "select".length(), query.indexOf("from"));


                String[] c_names_part = c_names.split(",");
                header = c_names.trim().replaceAll("\\s+", "").replaceAll(",", "|");
                data.add(header);


                // get column numbers
                for (String cn : c_names_part) {
                    for (int k=0; k<column_names.size(); k++) {
                        if (cn.trim().equals(column_names.get(k).trim())) {
                            column_numbers.add(k);
                        }
                    }
                }

                int column_number=0;
                for (int j=0; j<column_names.size(); j++) {
                    if (column_names.get(j).equals(column_name)) {
                        column_number = j;
                        break;
                    }
                }

                File file = new File(file_path);
                Scanner scanner = new Scanner(file);
                while (scanner.hasNextLine()) {
                    String d = scanner.nextLine();
                    String[] d_parts = d.split("\\|");
                    if (d_parts[column_number].equals(value)) {
                        String v = "";
                        for (int number : column_numbers) {
                            v += d_parts[number] + "|";
                        }
                        v = v.substring(0, v.length()-1);
                        data.add(v);
                    }
                }
                scanner.close();

            } else {
                // where is not included

                // get require column names and number
                List<Integer> column_numbers = new ArrayList<Integer>();
                String c_names = query.substring(query.indexOf("select ") + "select".length(), query.indexOf("from"));

                String[] c_names_part = c_names.split(",");
                header = c_names.trim().replaceAll("\\s+", "").replaceAll(",", "|");
//                data.add(header);

                // get column numbers
                for (String cn : c_names_part) {
                    for (int k=0; k<column_names.size(); k++) {
                        if (cn.trim().equals(column_names.get(k).trim())) {
                            column_numbers.add(k);
                        }
                    }
                }

                File file = new File(file_path);
                Scanner scanner = new Scanner(file);
                while (scanner.hasNextLine()) {
                    String d = scanner.nextLine();
                    String[] d_parts = d.split("\\|");

                    String v = "";
                    for (int number : column_numbers) {
                        v += d_parts[number] + "|";
                    }
                    v = v.substring(0, v.length()-1);
                    data.add(v);
                }
                scanner.close();
            }

        }
        System.out.println(data);
    }

    public static void executeUpdate(String query) {}

    public static void executeDelete(String query) {}

    public static void executeQuery(String query) throws IOException {
        query = formatQuery(query.toLowerCase().trim());

        String queryType = query.split("\\s+")[0];


        switch (queryType) {
            case "create":
//                System.out.println("CREATE Query");
                executeCreate(query);
                break;
            case "use":
//                System.out.println("USE Query");
                executeUse(query);
                break;
            case "insert":
//                System.out.println("INSERT Query");
                executeInsert(query);
                break;
            case "select":
//                System.out.println("SELECT Query");
                executeSelect(query);
                break;
            case "update":
//                System.out.println("UPDATE Query");
                executeUpdate(query);
                break;
            case "delete":
//                System.out.println("DELETE Query");
                executeDelete(query);
                break;
            default:
                System.out.println("INVALID QUERY! - " + queryType.toUpperCase());
        }
    }
}
