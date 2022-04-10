package Connection;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import java.io.ByteArrayOutputStream;

public class SSHConnect {

    public static void main(String []args) throws Exception {
        listFolderStructure("gcpKey", "123qwe", "34.136.116.68", 22, "java -jar D2_DB-1.0-SNAPSHOT.jar");
        //listFolderStructure("gcpKey", "", "34.136.127.213", 22, "dir");
    }

    public static void listFolderStructure(String username, String password,
                                           String host, int port, String command) throws Exception {

        JSch jsch=null;
        Session session = null;
        ChannelExec channel = null;

        try {
            jsch=new JSch();
            jsch.setKnownHosts("C:\\Users\\shara\\.ssh/known_hosts");
            jsch.addIdentity("C:\\Users\\shara\\.ssh/id_rsa");
            session = jsch.getSession(username, host, port);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");

            session.connect();

            channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand(command);
            ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
            channel.setOutputStream(responseStream);
            channel.connect();

            while (channel.isConnected()) {
                Thread.sleep(100);
            }

            String responseString = new String(responseStream.toByteArray());
            System.out.println(responseString);
        } finally {
            if (session != null) {
                session.disconnect();
            }
            if (channel != null) {
                channel.disconnect();
            }
        }
    }
}
