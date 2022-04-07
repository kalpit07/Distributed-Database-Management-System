package queryimplementation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class executeUp
{
    public static void demo(String query, String dbName)
    {
        String[] querySplit = query.split(" ");
        String tableName = querySplit[1].trim();
        String tablePath = "VM1/" + dbName + "/" + tableName + ".txt";

        int indexOfSet = query.indexOf("set");
        int indexOfWhere = query.indexOf("where");

        String columnAndValue = query.substring(indexOfSet + 4, indexOfWhere - 1);
        columnAndValue = columnAndValue.trim();

        String condition = querySplit[querySplit.length - 1];
        condition = condition.substring(0, condition.length() - 1);

        String[] temp = columnAndValue.split("=");
        String updateColumn = temp[0].trim();
        String updateValue = temp[1].trim();
        if(updateValue.startsWith("'") || updateValue.startsWith("\""))
        {
            updateValue = updateValue.substring(1, updateValue.length() - 1);
        }

        String[] conditionBreak = condition.split("=");
        String conditionColumn = conditionBreak[0];
        String conditionValue = conditionBreak[1];
        if(conditionValue.startsWith("'") || conditionValue.startsWith("\""))
        {
            conditionValue = conditionValue.substring(1, conditionValue.length() - 1);
        }

        boolean answer = true;

        try
        {
            String line = "";
            File metadata = new File("VM1/" + dbName + "/" + dbName + "_metadata.txt");
            BufferedReader checkForUpdateType = new BufferedReader(new FileReader(metadata));
            while((line = checkForUpdateType.readLine()) != null)
            {
                String[] allLines = line.split("\n");
                for(String everyLine : allLines)
                {
                    String[] values = everyLine.split("\\|");
                    if(values[0].equals(tableName))
                    {
                        String columnsAndTypes = values[1];
                        String[] types = columnsAndTypes.split(",");
                        for(int j = 0; j < types.length; j++)
                        {
                            if(types[j].startsWith(updateColumn))
                            {
                                String[] isPkOrFk = types[j].split(" ");
                                if(isPkOrFk.length == 3 || isPkOrFk.length == 5)
                                {
                                    if(isPkOrFk[2].equals("PRIMARY_KEY"))
                                    {
                                        System.out.println("You are trying to update a primary key which is not possible.");
                                        System.out.println("The query cannot be executed.");
                                        answer = false;
                                    }
                                    else if(isPkOrFk[2].equals("FOREIGN_KEY"))
                                    {
                                        System.out.println("You are trying to update a foreign key which is not possible.");
                                        System.out.println("The query cannot be executed");
                                        answer = false;
                                    }
                                }

                                else if(isPkOrFk.length == 2)
                                {
                                    answer = true;
                                }
                            }
                        }
                    }
                }
            }

            checkForUpdateType.close();

            if(answer)
            {
                String overWrite = "";
                String tableRow = "";
                int indexOfConditionColumn = -1;
                int indexOfUpdateColumn = -1;
                File table = new File(tablePath);
                BufferedReader updating = new BufferedReader(new FileReader(table));
                int count = 0;
                String tableHeader = updating.readLine();
                overWrite += tableHeader;
                overWrite += "\n";
                String[] headerValues = tableHeader.split("\\|");
                for(int x = 0; x < headerValues.length; x++)
                {
                    if(headerValues[x].equals(conditionColumn))
                    {
                        indexOfConditionColumn = x;
                    }

                    if(headerValues[x].equals(updateColumn))
                    {
                        indexOfUpdateColumn = x;
                    }
                }

                while((tableRow = updating.readLine()) != null)
                {
                    String[] rowValues = tableRow.split("\\|");
                    if(rowValues[indexOfConditionColumn].equals(conditionValue))
                    {
                        rowValues[indexOfUpdateColumn] = updateValue;
                    }

                    for(int x = 0; x < rowValues.length; x++)
                    {
                        if(x == rowValues.length - 1)
                        {
                            overWrite += rowValues[x];
                        }
                        else
                        {
                            overWrite += rowValues[x];
                            overWrite += "|";
                        }
                    }
                    overWrite += "\n";
                }

                updating.close();

                FileWriter writer = new FileWriter(tablePath, false);
                writer.write(overWrite);
                writer.close();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void main(String[] args)
    {
        String query = "update students set program='MOM' where name='vishnu';";
        String dbName = "testdb";
        demo(query, dbName);
    }
}
