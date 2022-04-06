package Logging;
/*
 * General Logs: query execution time, state of the database
 * (e.g. how many tables are there with number of records
 * at a given time)
 * */

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

public class GeneralLogging {

    private static long startTime;
    private static long stopTime;

    private static long currentTime() {
        return System.nanoTime();
    }

    private static String currentDateTime() {
        StringBuilder result = new StringBuilder();
        result.append("Date: ").append(LocalDate.now()).append(" ");
        result.append("Time: ").append(LocalTime.now()).append(" ");
        return result.toString();
    }

    private static void startTimer() {
        startTime = currentTime();
    }

    private static void stopTimer() {
        stopTime = currentTime();
    }

    private static long calculateTimeDifference() {
        return stopTime - startTime;
    }

    private static void logQueryExecutionTime() {
        System.out.println("Execution Time (in NanoSeconds) - "+ GeneralLogging.calculateTimeDifference());

    }

    public static void createAFileForGeneralLogs(String messageToLog) {

        try {
            File file = new File("LogStorage" + "\\" + "General_Logs.log");
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, true));
            bufferedWriter.write(GeneralLogging.currentDateTime() + messageToLog + "\n");
            bufferedWriter.close();
        }catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String args[]){
        GeneralLogging.startTimer();
        GeneralLogging.stopTimer();
        GeneralLogging.createAFileForGeneralLogs("Started SQL Query Execution");
        GeneralLogging.logQueryExecutionTime();
    }
}