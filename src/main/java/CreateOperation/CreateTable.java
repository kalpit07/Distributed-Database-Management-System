package CreateOperation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateTable {
    private HashMap<String, String> hashMapToStoreTableData = new HashMap<String, String>();
    public String tableName;
    private String path1;
    private String path2;
    private List<String> constraintsList = new ArrayList<>();
    private String databasePath = null;
    private String databaseName = null;

    // database path, database name will be the arguments
    public CreateTable(String databasePath) {
        this.databasePath = databasePath;
    }

    // Assumption that there is no space between "(" bracket and table name
    public void identifyTheCreateQueryElements(String userQuery) {
        // Convert string to remove spaces from the beginning and the end
        userQuery = userQuery.trim();
        // Create query is of the form
        // CREATE TABLE Persons(PersonID int,LastName varchar(255),FirstName varchar(255),Address varchar(255),City varchar(255));
        String[] createTableQuerySplitByComma = userQuery.split(",");

        String[] listOfElementsDividedBySpace = createTableQuerySplitByComma[0].split(" ");


        tableName = listOfElementsDividedBySpace[2];

        if(tableName.contains("(")) {

            String[] listDividedByOpeningBracket = (listOfElementsDividedBySpace[2].split("\\("));
            tableName = listDividedByOpeningBracket[0].trim();

        }

        String[] listToFindFirstTableElementAndItsType = createTableQuerySplitByComma[0].split("\\(");

        String removeSpacesToFindFirstTableElement = listToFindFirstTableElementAndItsType[1].trim();

        String[] listContainingTheFirstTableElementAndType = removeSpacesToFindFirstTableElement.split(" ");

        StringBuilder temp3ListResultString = new StringBuilder();

        temp3ListResultString.append(listContainingTheFirstTableElementAndType[1]).append("");

        if(listContainingTheFirstTableElementAndType.length > 2) {
            int j = 2;
            while(j<listContainingTheFirstTableElementAndType.length) {
                temp3ListResultString.append(listContainingTheFirstTableElementAndType[j]).append("");
                j++;
            }
        }

        hashMapToStoreTableData.put(listContainingTheFirstTableElementAndType[0], temp3ListResultString.toString());


        for(int i=1;i<createTableQuerySplitByComma.length;i++) {

            String checkIfTableElementIsPrimaryKey = createTableQuerySplitByComma[i].trim().split(" ")[0];

            String checkIfTableElementIsForeignKey = createTableQuerySplitByComma[i].trim().split(" ")[0];

            if(checkIfTableElementIsPrimaryKey.equalsIgnoreCase("primary")) {

                // add all the constraints to the table columns list
                constraintsList.add(createTableQuerySplitByComma[i].trim());
                continue;
            }

            else if(checkIfTableElementIsForeignKey.equalsIgnoreCase("foreign")) {
                // add all the constraints to the table columns list
                constraintsList.add(createTableQuerySplitByComma[i].trim());
                continue;
            }

            String[] listContainingTableElementAndItsType = createTableQuerySplitByComma[i].trim().split(" ");

            StringBuilder valueResultString = new StringBuilder();

            valueResultString.append(listContainingTableElementAndItsType[1].trim()).append("");

            if(listContainingTableElementAndItsType.length > 2) {
                // Starting from 1 as 0 will be key and array elements after 0 will be part of the
                int j = 2;
                while(j<listContainingTableElementAndItsType.length) {
                    valueResultString.append(listContainingTableElementAndItsType[j]).append("");
                    j++;
                }
            }
            hashMapToStoreTableData.put(listContainingTableElementAndItsType[0].trim(), valueResultString.toString());
        }
        // Print the hashmap
        for(Map.Entry<String,String> entry: hashMapToStoreTableData.entrySet()) {
            System.out.println("Key =" + entry.getKey() + " ,Value is:" + entry.getValue());
        }
    }

    public void createAFileForTypeOfAttributesOfTheTable() {

        try {
            File file = new File(databasePath + "//" + "db1" + "-metadata.txt");
            if(file.createNewFile()) {
                // add a logger here to say that the file is created
                System.out.println("File Created:" + file.getName());
            }
            else {
                System.out.println("File already exists");
            }
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file,true));
            bufferedWriter.write( "\n" + tableName + "\n");
            // To create one file where we store the data types
            for(Map.Entry<String,String> entry: hashMapToStoreTableData.entrySet()) {
                bufferedWriter.write(entry.getKey()+","+entry.getValue()+"|");
            }
            for(int i=0; i<constraintsList.size();i++) {
                bufferedWriter.write(constraintsList.get(i)+"|");
            }
            bufferedWriter.close();
        }catch ( IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void createAFileForValuesOfTheTable() {

        try {
            File file = new File(databasePath + "//" + tableName + "-Values.txt");
            if (file.createNewFile()) {
                System.out.println("File was Created:" + file.getName());
            } else {
                System.out.println("The file is already present, Please try with a different name for the table.");
            }
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, false));
            // To create one file where we store the data types
            int count = 1;
            for (Map.Entry<String, String> entry : hashMapToStoreTableData.entrySet()) {
                if(count == hashMapToStoreTableData.size()){
                    bufferedWriter.write(entry.getKey());
                }else{
                    bufferedWriter.write(entry.getKey() + "|");
                }
                count++;
            }
            bufferedWriter.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void pushMetaData() {
        //get the database name from use.java class and pass it to the above constructor and use it here
        //we will get the table name here, so just open the dbmeta.txt file that is created in createdatabase class and append
        //those two values here separated by pipe.
    }

    public static void main(String[] args) {
        String userQuery = "CREATE TABLE persons(PersonID int,LastName varchar(255),FirstName varchar(255),Address varchar(255),City varchar(255));";
        // CREATE TABLE Persons( ID int NOT NULL, LastName varchar(255) NOT NULL, FirstName varchar(255), Age int, PRIMARY KEY (ID) );
        // VM1/Persons/Persons-Value.txt , Persons-Datatype.txt
        CreateTable createTable = new CreateTable("VM1//db1");
        createTable.identifyTheCreateQueryElements(userQuery);
        createTable.createAFileForTypeOfAttributesOfTheTable();
        createTable.createAFileForValuesOfTheTable();
    }
}
