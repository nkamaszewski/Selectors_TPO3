import java.io.IOException;
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
    ServerSocketChannel serverSocketChannel = null;
    Selector selector = null;
    String serverHost = "localhost";
    int serverPort = 50000;

    Map<String, List<SocketChannel>> subscribersMap = new TreeMap<String, List<SocketChannel>>();


    public Server(){

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
                        System.out.println("client connected to server");
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

    public void serviceRequest(SocketChannel clientSocketChannel){
        if(!clientSocketChannel.isOpen()) {
            return;
        }

        StringBuffer requestBuffer = MyUtils.readResponse(clientSocketChannel);

        try {

            String[] request = Pattern.compile(";").split(requestBuffer);

            String requestMethod = request[0];
            String requestPayload = request[1];

            System.out.println("req method: "+ requestMethod + "  payload: " + requestPayload);
            //////////////////////////////////// add subject
            if(requestMethod.equals("addSubject")){
                System.out.println(requestPayload);
                if(!subscribersMap.containsKey(requestPayload)){
                    subscribersMap.put(requestPayload, new ArrayList<>());
                }
            }
            ///////////////////////////////////// get subjects
            else if(requestMethod.equals("getSubjects")){
                try {
                    String subjects = new String();

                    for (String s : subscribersMap.keySet()){
                        subjects += s+";";
                    }

                    byte[] message = new String(subjects+"\n").getBytes();
                    ByteBuffer bb = ByteBuffer.wrap(message);
                    clientSocketChannel.write(bb);
                    bb.clear();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            /////////////////////////////////// add subsciber
            else if(requestMethod.equals("addSubscriber")){
                System.out.println(subscribersMap.get("kot"));
                if(subscribersMap.containsKey(requestPayload)){
                    List<SocketChannel> list = subscribersMap.get(requestPayload);
                    System.out.println("adding subscriber");
                    if(list.indexOf(clientSocketChannel) == -1){
                        list.add(clientSocketChannel);
                        System.out.println("subscriber added");
                    };
                }
            }
            /////////////////////////////////// delete subscriber
            else if(requestMethod.equals("deleteSubscriber")){
                if(subscribersMap.containsKey(requestPayload)){
                    List<SocketChannel> list = subscribersMap.get(requestPayload);
                    if(list.indexOf(clientSocketChannel) == -1){
                        list.remove(clientSocketChannel);
                    };
                }
            }
            ///////////////////////////////// sending news to subscribers
            else if(requestMethod.equals("postNews")){
                if(subscribersMap.containsKey(requestPayload)){
                    List<SocketChannel> list = subscribersMap.get(requestPayload);
                    System.out.println("posting news... subscribers: " + list.size());
                    for(SocketChannel sc : list){
                        try {
                            byte[] message = new String(requestPayload + ";NEWS: " + request[2] + "\n").getBytes();
                            ByteBuffer bb = ByteBuffer.wrap(message);
                            sc.write(bb);
                            bb.clear();
                        } catch (Exception e){
                            System.out.println("Exception in posting news");
                        }
                    }
                }
            }

        } catch (Exception e){
            e.printStackTrace();
            try { clientSocketChannel.close();
                clientSocketChannel.socket().close();
            } catch (Exception exception) {}
        }

    }

    public static void main(String[] args) {
        Server server = new Server();

    }

}
