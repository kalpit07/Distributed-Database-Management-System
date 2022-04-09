package logmanagement;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class LogManagement {
    public static void main(String args[]) {
        String s = getTime();
        System.out.println(s);
    }

    public static String getTime() {
        String dBTime = LocalDate.now() + " " + LocalTime.now();
        return dBTime;
    }


    public static FileWriter queryLogger(String username, String query) throws Exception {
        FileWriter queryFile = new FileWriter("VM/logFiles/queryLog.txt", true);
        queryFile.append(getTime() + " : " + username + " : " + query + "\n");
        queryFile.close();
        return queryFile;
    }

    public void eventLogger(String database) throws Exception {
        FileWriter fileWriter = new FileWriter("VM/logFiles/eventLog.txt", true);
        fileWriter.append("Database has been changed to : " + database + "\n");
        fileWriter.close();
    }

    public static void crashLogger(Exception exp) throws IOException {
        FileWriter fileWriter = new FileWriter("VM/logFiles/eventLog.txt", true);
        fileWriter.append("Crash Occurred: " + exp + "\n");
        fileWriter.close();

    }

    public void transactionLogger(String msg) throws Exception {
        FileWriter fileWriter = new FileWriter("VM/logFiles/eventLog.txt", true);
        fileWriter.append(msg + "\n");
        fileWriter.close();
    }

    public static void generalLog(String database, Long execTime) throws Exception {

        FileWriter fileWriter = new FileWriter("VM/logFiles/generalLog.txt", true);
        String dirPath="VM/" + database + "/";
        File fileIterate= new File(dirPath);
        int count=0;
        String str[] = fileIterate.list();
        int noOFLines = 0;
        for (int i = 0; i < str.length; i++) {
            String s = str[i];
            File check = new File(fileIterate, s);
            if (check.isFile()) {
                count++;
                File table = new File(dirPath + check.getName());
                BufferedReader br = new BufferedReader(new FileReader(table));
                while (br.readLine() != null) noOFLines++;
            }
        }
        fileWriter.append("Database has "+count+" tables with "+noOFLines+" records at - "+getTime()+"\n");
        fileWriter.append("Execution time - "+execTime+"\n");
        fileWriter.close();
    }

}
