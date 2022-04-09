package DataModel;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static queryimplementation.QueryImplementation.*;


public class datamodel {

    public static void main(String args[]){
        try {
            Scanner input=new Scanner(System.in);
            System.out.println("Enter the database name:");
            String s=input.nextLine();
            dataModel(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void dataModel(String database) throws IOException {
        File VM = new File(BASE_DIRECTORY);
        List<String> checkDB = List.of(VM.list());
        String em = getFromMetadata(database);
        if(em.contains(database)){
            if(checkDB.contains(database)) {
                String s = getMetadata(database);
                String[] names = s.split("\n");
                FileWriter fileWriter = fileWrite(database);
                fileWriter.write("\nInstance: " + em.split("\\|")[1].trim());
                for (String si: names) {
                    String[] nameOfTable = si.split("\\|");
                    List<String> column_names = new ArrayList<>();
                    List<String> column_types = new ArrayList<>();
                    List<String> column_keys = new ArrayList<>();
                    List<String> cardinality = new ArrayList<>();
                    columnGenerate(nameOfTable, column_names, column_types, column_keys, cardinality);
                    tableFormat(fileWriter, nameOfTable, column_names, column_types, column_keys, cardinality);
                }
                fileWriter.close();
        }else System.out.println("Database Not Found!");
        }else System.out.println("Database Not Found!");
        }

    private static String getFromMetadata(String database) throws IOException {
        String em="";
        BufferedReader br = new BufferedReader(new FileReader("VM/Global_Data_Dictionary.txt"));
        String line = "";
        do {
            if(line.contains(database)) {
                em += line + "\n";
            }
        } while ((line = br.readLine()) != null);
        return em;
    }

    private static void columnGenerate(String[] nameOfTable, List<String> column_names, List<String> column_types, List<String> column_keys, List<String> cardinality) {
        String[] group = nameOfTable[1].split("\\,");
        for (String s2: group) {
            String[] split = s2.split("\\s+");
            column_names.add(split[0]);
            column_types.add(split[1]);
            if(split.length>2) {
                column_keys.add(split[2]);
                //System.out.println(split[2]);
            }
            if(split.length>3){
                String s = "";
                String str = split[4];
                String answer = str.substring(str.indexOf("(")+1, str.indexOf(")"));
                s += nameOfTable[0] + "|" + split[0] + "   ->   " + split[4].replaceAll("\\(.*\\)", "" + "|" + answer);
                cardinality.add(s);
            }
        }
    }

    private static void tableFormat(FileWriter fileWriter, String[] nameOfTable, List<String> column_names, List<String> column_types, List<String> column_keys, List<String> cardinality) throws IOException {
        fileWriter.write("\n\nTable: " + nameOfTable[0]);
        fileWriter.write("\n------------------------------------------------");
        fileWriter.write(String.format("\n|%14s| %14s| %14s|","Column Name","Data Type", "Key Name"));
        fileWriter.write("\n------------------------------------------------");
        int count = 0;
        for(int d = 0; d< column_names.size(); d++) {
            if (column_keys.size()>count) {
                //System.out.println("for" + column_keys.get(d));
                fileWriter.write(String.format("\n|%14s| %14s| %14s|", column_names.get(d), column_types.get(d), column_keys.get(d)));
            }else {
                fileWriter.write(String.format("\n|%14s| %14s| %14s|", column_names.get(d), column_types.get(d), ""));

            }
            count++;
        }

        fileWriter.write("\n------------------------------------------------\n");
        fileWriter.write("Relationship with:  "  );
        for (String s: cardinality) {
            fileWriter.write(s);
        }
    }

    private static FileWriter fileWrite(String database) throws IOException {
        FileWriter fileWriter = new FileWriter("VM/DataModelling/" + database + "_ERD.txt", false);
        fileWriter.write("Database: " + database);
        return fileWriter;
    }

    private static String getMetadata(String database) throws IOException {
        String s="";
        File metaData = new File("VM/" + database + "/" + database + "_metadata.txt");
        BufferedReader br = new BufferedReader(new FileReader(metaData));
        String line = "";
        if ((line = br.readLine()) != null) {
            do {
                s += line + "\n";
            } while ((line = br.readLine()) != null);
        }
        return s;
    }
}

