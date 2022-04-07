package Logging;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class QueryLogging {

    private static String currentTime() {
//        return System.nanoTime();
        return GeneralLogging.currentDateTime();
    }
    public static void logUserQuery(String userQuery) {
        System.out.println("Executed Query: " + userQuery + " at " + currentTime());
        QueryLogging.createAFileForQueryLogs("Executed Query: " + userQuery);
    }

    public static void createAFileForQueryLogs(String messageToLog) {
        try {
            File file = new File("LogStorage" + "\\" + "Query_Logs.log");
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, true));
            bufferedWriter.write(QueryLogging.currentTime() + messageToLog + "\n");
            bufferedWriter.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }


    public static void main(String[] args) {
        QueryLogging.logUserQuery("Create statement execution");
    }
}
