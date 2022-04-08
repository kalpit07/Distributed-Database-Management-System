package datadump;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import static queryimplementation.QueryImplementation.*;

public class DataDumpHandler {


    public enum GenerateType{
        DATABASE,
        TABLE
    }

    public DataDumpHandler(){


    }

    public void recordGenerateQuery(GenerateType generateType, String entityName, String query, String path) throws IOException {
        switch (generateType){
            case DATABASE -> System.out.println("Database is being generated");
            case TABLE -> {
                System.out.println("Table is being generated" + entityName + query);
                FileWriter fw_local = new FileWriter(path, true);
                fw_local.write(entityName+"_create" + "|" + query + "\n");
                fw_local.close();
            }
        }
    }


    public void exportDump(String databaseName) throws FileNotFoundException {
        String filePath=BASE_DIRECTORY+databaseName+"/"+databaseName+"_metadata.txt";
        //System.out.println( " file path is :"+filePath);

        StringBuilder queryBuilder=new StringBuilder();


        File metadata = new File(filePath);
        Scanner reader = new Scanner(metadata);
        while(reader.hasNextLine()) {
            System.out.println("===========================================");
            queryBuilder=new StringBuilder();
            queryBuilder.append("CREATE TABLE ");
            String line = reader.nextLine();
            System.out.println(line);
            String[] line_parts = line.split("\\|");
            String tableName=line_parts[0];
            queryBuilder.append(tableName);
            queryBuilder.append(" (");
            String columnInfo=line_parts[1];
            System.out.println("tableName "+tableName);
            System.out.println("columnInfo "+columnInfo);

            String[] column_parts = columnInfo.split(",");
            String []primaryKeyInfo=new String[3];
            String []foreignKeyInfo=new String[5];
            for (String parts:column_parts) {
                String []colInfo = parts.split("\\s");
                int length = colInfo.length;
                System.out.println(parts+ " length : " + length);
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
                System.out.println(queryBuilder.toString());
                continue;
            }
            else if(foreignKeyInfo[0]==null){
                queryBuilder.append("PRIMARY KEY (");
                queryBuilder.append(primaryKeyInfo[0]);
                queryBuilder.append("));");
                System.out.println(queryBuilder.toString());
                continue;
            }
            System.out.println(queryBuilder.toString());
            queryBuilder.append("PRIMARY KEY (");
            queryBuilder.append(primaryKeyInfo[0]);
            queryBuilder.append("), ");
            queryBuilder.append("FOREIGN KEY (");
            queryBuilder.append(foreignKeyInfo[0]);
            queryBuilder.append(") REFERENCES ");
            queryBuilder.append(foreignKeyInfo[4]);
            queryBuilder.append(");");

            System.out.println(queryBuilder.toString());
        }

    }
}
