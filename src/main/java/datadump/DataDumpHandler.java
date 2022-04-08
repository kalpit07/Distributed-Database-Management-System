package datadump;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static queryimplementation.QueryImplementation.*;

public class DataDumpHandler {

    List<String> queries=new ArrayList<>();
    public static String VM_INSTANCE = "DUMP/";

    public enum GenerateType{
        DATABASE,
        TABLE
    }

    public DataDumpHandler(){


    }

    public void writeGenerateQuery( String path, String pathFile) throws IOException {

        File theDir = new File(path);

        System.out.println(path);
        if (!theDir.exists()) {

            boolean result = false;

            try {

                theDir.mkdirs();
                FileWriter fw_local = new FileWriter(pathFile, true);
                System.out.println(queries.size());
                for (String query:queries) {

                    fw_local.write(query);
                }

                fw_local.close();
                result = true;
            } catch (SecurityException se) {
                // handle it
                System.out.println(se.getMessage());
            }
            if (result) {
                System.out.println("Folder created");
            }
        } else if (theDir.exists()) {
            FileWriter fw_local = new FileWriter(pathFile, true);
            System.out.println(queries.size());
            for (String query:queries) {

                fw_local.write(query);
            }

            fw_local.close();
            System.out.println("Folder exist");
        }

        FileWriter fw_local = new FileWriter(path, true);
        for (String query:queries) {

            fw_local.write(query);
        }

        fw_local.close();
    }


    public void exportDump(String databaseName) throws IOException {
        String filePath=BASE_DIRECTORY+databaseName+"/"+databaseName+"_metadata.txt";
        generateCreateQuery(filePath);
        //writeGenerateQuery(BASE_DIRECTORY+VM_INSTANCE+databaseName,BASE_DIRECTORY+VM_INSTANCE+databaseName+"/dump.txt");
        //generateInsertQuery( filePath);
    }

    void generateInsertQuery(String filePath) throws FileNotFoundException {
        File metadata = new File(filePath);
        Scanner reader = new Scanner(metadata);
        while(reader.hasNextLine()){
            String line = reader.nextLine();
            String[] line_parts = line.split("\\|");
            String tableName=line_parts[0];
        }
    }


    void generateCreateQuery(String filePath) throws FileNotFoundException {
        StringBuilder queryBuilder=new StringBuilder();


        File metadata = new File(filePath);
        Scanner reader = new Scanner(metadata);
        while(reader.hasNextLine()) {
            System.out.println("===========================================");
            queryBuilder=new StringBuilder();
            queryBuilder.append("CREATE TABLE ");
            String line = reader.nextLine();
            String[] line_parts = line.split("\\|");
            String tableName=line_parts[0];
            queryBuilder.append(tableName);
            queryBuilder.append(" (");
            String columnInfo=line_parts[1];


            String[] column_parts = columnInfo.split(",");
            String []primaryKeyInfo=new String[3];
            String []foreignKeyInfo=new String[5];
            for (String parts:column_parts) {
                String []colInfo = parts.split("\\s");
                int length = colInfo.length;
                if(length==3){
                    primaryKeyInfo=colInfo;
                }else if(length==5){
                    foreignKeyInfo=colInfo;
                }
                for(int i=0;i<2;i++ ){
                    queryBuilder.append(colInfo[i]);
                    if(i==1){
                        queryBuilder.append(",");
                    }
                    queryBuilder.append(" ");

                }

            }
            if(primaryKeyInfo[0]==null && foreignKeyInfo[0]==null){
                queryBuilder.delete(queryBuilder.length()-2,queryBuilder.length()-1);
                queryBuilder.append(");");
                queries.add(queryBuilder.toString());

                continue;
            }
            else if(foreignKeyInfo[0]==null){
                queryBuilder.append("PRIMARY KEY (");
                queryBuilder.append(primaryKeyInfo[0]);
                queryBuilder.append("));");
                queries.add(queryBuilder.toString());
                continue;
            }

            queryBuilder.append("PRIMARY KEY (");
            queryBuilder.append(primaryKeyInfo[0]);
            queryBuilder.append("), ");
            queryBuilder.append("FOREIGN KEY (");
            queryBuilder.append(foreignKeyInfo[0]);
            queryBuilder.append(") REFERENCES ");
            queryBuilder.append(foreignKeyInfo[4]);
            queryBuilder.append(");");

            queries.add(queryBuilder.toString());

        }
    }
}
