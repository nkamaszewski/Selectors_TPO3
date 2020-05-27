import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

import static java.lang.System.*;

public class MyUtils {

    public static StringBuffer readResponse(SocketChannel socketChannel) {
        long startTime = currentTimeMillis();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        byteBuffer.flip();
        byteBuffer.clear();
        StringBuffer responseBuffer = new StringBuffer();
        try {
            readLoop:
            while (true) {
                int i = socketChannel.read(byteBuffer);
                out.println(i);
                if (i > 0) {
                    byteBuffer.flip();
                    CharBuffer charBuffer = Charset.forName("UTF-8").decode(byteBuffer);
                    while (charBuffer.hasRemaining()) {
                        char c = charBuffer.get();
                        if (c == '\r' || c == '\n') {
                            break readLoop;
//                            break;
                        }
                        responseBuffer.append(c);
                    }
                }

                /////////////// timeout 1s
                if(currentTimeMillis()-startTime>1000){
                    break;
                }
            }
        } catch (Exception e) {
            out.println("error in readResponse");
        }
        return responseBuffer;
    }
}
