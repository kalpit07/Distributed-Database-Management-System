package datadump;

import queryimplementation.QueryImplementation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import static queryimplementation.QueryImplementation.*;
import static queryimplementation.QueryImplementation.DATABASE;

public class DataDumpHandler {


    public DataDumpHandler(){


    }

    public void recordGenerateQuery(QueryImplementation.GenerateType generateType, String entityName, String query, String path) throws IOException {
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
        System.out.println(filePath);

        File metadata = new File(filePath);
        Scanner reader = new Scanner(metadata);
        while(reader.hasNextLine()) {
            String line = reader.nextLine();
            System.out.println("2========================2");
            System.out.println(line);
            String[] line_parts = line.split("\\|");

            String queryTag=databaseName+"_create";
            if (line_parts[0].equals(queryTag)) {
                System.out.println("***   "+line_parts[1]);
            }
        }
    }
}
