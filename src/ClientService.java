import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.regex.Pattern;

public class ClientService {
    SocketChannel clientSocketChannel = null;

    public ClientService(){
        try {
            clientSocketChannel = SocketChannel.open(new InetSocketAddress("localhost", 50000));
            System.out.println("Client connected to server at port 50000");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getSubjects() {
        try {
            byte[] message = new String("getSubjects;empty;\n").getBytes();
            ByteBuffer byteBuffer = ByteBuffer.wrap(message);
            clientSocketChannel.write(byteBuffer);
            byteBuffer.clear();

            if(!clientSocketChannel.isOpen()) {
                return "error";
            }
            StringBuffer responseBuffer = MyUtils.readResponse(clientSocketChannel);

            String[] response = Pattern.compile(";").split(responseBuffer);

            String result = "";

            for(String r : response){
                result += r + ", ";
            }
            return result;

        } catch (IOException e) {
            e.printStackTrace();
            return "error";
        }
    }

    public void subscribe(String subject){
        try {
        byte[] message = new String("addSubscriber;" + subject + ";\n").getBytes();
        ByteBuffer byteBuffer = ByteBuffer.wrap(message);
        clientSocketChannel.write(byteBuffer);
        byteBuffer.clear();

        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    public void unsubscribe(String subject){
        try {
            byte[] message = new String("deleteSubscriber;" + subject + ";\n").getBytes();
            ByteBuffer byteBuffer = ByteBuffer.wrap(message);
            clientSocketChannel.write(byteBuffer);
            byteBuffer.clear();

        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    public String getNews(){
        StringBuffer responseBuffer = MyUtils.readResponse(clientSocketChannel);

        String[] response = Pattern.compile(";").split(responseBuffer);

        String result = "Subject: " + response[0] + ", " + response[1];

        return result;
    }
}
