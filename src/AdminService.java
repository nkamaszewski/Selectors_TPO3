import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class AdminService {
    SocketChannel adminSocketChannel = null;

    public AdminService(){
        try {
            adminSocketChannel = SocketChannel.open(new InetSocketAddress("localhost", 50000));
            System.out.println("admin connected to server at port 50000");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addSubject(String subject) {
        System.out.println("sending subject to add: " + subject);
        try {
            byte[] message = new String("addSubject;"+subject+"\n").getBytes();
            ByteBuffer byteBuffer = ByteBuffer.wrap(message);
            adminSocketChannel.write(byteBuffer);
            byteBuffer.clear();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteSubject(String subject) {
        System.out.println("sending subject to delete: " + subject);
        try {
            byte[] message = new String("deleteSubject;"+subject+"\n").getBytes();
            ByteBuffer byteBuffer = ByteBuffer.wrap(message);
            adminSocketChannel.write(byteBuffer);
            byteBuffer.clear();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendNews(String subject, String news){
        System.out.println(subject);
        try {
            byte[] message = new String("postNews;"+subject+";" + news +"\n").getBytes();
            ByteBuffer byteBuffer = ByteBuffer.wrap(message);
            adminSocketChannel.write(byteBuffer);
            byteBuffer.clear();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
