package Logging;

public class QueryLogging {

    private long currentTime() {
        return System.nanoTime();
    }
    public void logUserQuery(String userQuery) {
        System.out.println("Executed Query: " + userQuery + " at time:" + currentTime());
    }
}
