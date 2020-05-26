import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.*;
import java.util.regex.Pattern;

public class Server {
    public Server(){ }

    public static void main(String[] args) {
        ServerSocketChannel serverSocketChannel = null;
        Selector selector = null;
        String serverHost = "localhost";
        int serverPort = 50000;

        Map<String, List<SocketChannel>> subscribersMap = new TreeMap<String, List<SocketChannel>>();


        try{
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.socket().bind(new InetSocketAddress(serverHost,serverPort));

            selector = Selector.open();

            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        }
        catch (Exception e){
            System.out.println("exception");
            System.exit(1);
        }

        System.out.println("Server is running on port: " + serverPort);


        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // service connections
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////

        boolean runningServer = true;

        while (runningServer){

            try{

                selector.select();
                Set selectorKeys = selector.selectedKeys();
                Iterator iterator = selectorKeys.iterator();

                while(iterator.hasNext()){
                    SelectionKey selectionKey = (SelectionKey) iterator.next();
                    iterator.remove();

                    if(selectionKey.isAcceptable()){

                        SocketChannel clientSocketChannel = serverSocketChannel.accept();
                        clientSocketChannel.configureBlocking(false);

                        clientSocketChannel.register(selector, SelectionKey.OP_READ);
                        continue;
                    }

                    if(selectionKey.isReadable()){
                        SocketChannel clientSocketChannel = (SocketChannel) selectionKey.channel();
                        serviceRequest(clientSocketChannel);
                        continue;
                    }
                }


            } catch (Exception e){
                System.out.println("exception with single connection");
                continue;
            }
        }
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // service single request
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static void serviceRequest(SocketChannel clientSocketChannel){
        if(!clientSocketChannel.isOpen()) {
            return;
        }

        StringBuffer requestBuffer = new StringBuffer();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        try {
            readLoop:
            while (true){
                int i = clientSocketChannel.read(byteBuffer);
                if(i > 0){
                    byteBuffer.flip();
                    CharBuffer charBuffer = Charset.forName("UTF-8").decode(byteBuffer);

                    while (charBuffer.hasRemaining()){
                        char c = charBuffer.get();
                        if(c == '\r' || c == '\n'){
                            break readLoop;
                        }
                       requestBuffer.append(c);
                    }
                }
            }

            String[] request = Pattern.compile(";", 2).split(requestBuffer);

            if(request[0] == "dodaj"){
                // TO DO:
            }

        } catch (Exception e){
            e.printStackTrace();
            try { clientSocketChannel.close();
                clientSocketChannel.socket().close();
            } catch (Exception exception) {}
        }

    }

}
