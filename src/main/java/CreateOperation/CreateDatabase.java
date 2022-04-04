package CreateOperation;

import java.io.File;

public class CreateDatabase {
    String database;
    //add the path where the database folder needs to be created
    String DatabasePath = "";
    String query;


    public CreateDatabase(String query){
        this.query = query;
    }

    public void executeQuery(){
        String[] queryWords;
        queryWords = query.split(" ");
        database= queryWords[2];
        DatabasePath = DatabasePath+"/"+ database;
        File file = new File(DatabasePath);
        if (!file.exists()) {
            file.mkdir();
        }
        if(file.mkdir())
        {
            System.out.println("Folder with Database name successfully created");
        }

        else{
            System.out.println("Error in creating database");
        }
    }

}
