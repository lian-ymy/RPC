package com.example.rpccore.tcp;

import com.example.rpccore.server.HttpServer;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetServer;
import io.vertx.core.parsetools.RecordParser;

public class VertexTcpServer implements HttpServer {

    private byte[] handleRequest(byte[] requestData) {
        //在这里编写处理请求的逻辑，根据requestData构造相应数据并返回
        //这里只是一个示例，实际逻辑需要根据具体的业务需求来实现
        return "Hello, client".getBytes();
    }

    @Override
    public void doStart(int port) {
        //创建Vert.x实例
        Vertx vertx = Vertx.vertx();

        //创建TCP服务器
        NetServer server = vertx.createNetServer();

        //处理请求
        server.connectHandler(socket -> {
//            String textMessage = "Hello, jingliu! Hello, jingliu! Hello, jingliu! Hello jingliu!";
//            int messageLength = textMessage.getBytes().length;
//                if(buffer.getBytes().length < messageLength) {
//                    System.out.println("半包，length = " + buffer.getBytes().length);
//                    return;
//                }
//                if(buffer.getBytes().length > messageLength)  {
//                    System.out.println("粘包，length = " + buffer.getBytes().length);
//                    return;
//                }
//                String str = new String(buffer.getBytes(0,messageLength));
//                System.out.println(str);
//                if(textMessage.equals(str)) {
//                    System.out.println("好耶!");
//                }
            //构造parser
            RecordParser parser = RecordParser.newFixed(8);
            parser.setOutput(new Handler<Buffer>() {
                //初始化
                int size = -1;
                //一次完整的读取（头+体）
                Buffer resultBuffer = Buffer.buffer();

                @Override
                public void handle(Buffer buffer) {
                    if (-1 == size) {
                        //读取消息体长度
                        int size = buffer.getInt(4);
                        parser.fixedSizeMode(size);
                        //写入头信息到结果
                        resultBuffer.appendBuffer(buffer);
                    } else {
                        //写入头信息到结果
                        resultBuffer.appendBuffer(buffer);
                        System.out.println(resultBuffer.toString());
                        //重置一轮
                        parser.fixedSizeMode(8);
                        size = -1;
                        resultBuffer = Buffer.buffer();
                    }
                }
            });

            socket.handler(parser);
        });
        //启动TCP服务器并监听指定端口
        server.listen(port, result -> {
            if (result.succeeded()) {
                System.out.println("TCP server started on port" + port);
            } else {
                System.err.println("Failed to start TCP server: " + result.cause());
            }
        });
    }

    public static void main(String[] args) {
        new VertexTcpServer().doStart(8888);
    }
}
