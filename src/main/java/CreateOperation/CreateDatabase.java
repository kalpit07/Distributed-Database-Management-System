package CreateOperation;

import Utilities.Constants;

import javax.xml.crypto.Data;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

public class CreateDatabase {
    String databaseName;
    //add the path where the database folder needs to be created
    String DatabasePath = Constants.getVmPath();
    String query;

    public CreateDatabase(String query){
        this.query = query;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void executeQuery() {
        String[] queryWords;
        queryWords = query.split(" ");
        databaseName = queryWords[2];
        DatabasePath = DatabasePath + "//" + databaseName;
        File file = new File(DatabasePath);
        boolean isDatabaseCreated = file.mkdir();
        if(isDatabaseCreated) {
            // a file should be created for meta data
            createMetaDataFile();
            System.out.println("Folder with Database name successfully created");
        }
        else {
            System.out.println("Error in creating database");
        }
    }

    public void createMetaDataFile() {
        try {
            File file = new File(DatabasePath + "//" + databaseName + "-Meta.txt");
//            File file = new File("VM2//db3//db3"+ "-Meta.txt");
            if (file.createNewFile()) {
                System.out.println("File was Created:" + file.getName());
            } else {
                System.out.println("The file is already present, Please try with a different name for the meta data file.");
            }
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, true));
            // What goes into meta data file, comes over here
            bufferedWriter.write("");
            bufferedWriter.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        CreateDatabase db = new CreateDatabase("create database db2");
        db.executeQuery();
    }
}
