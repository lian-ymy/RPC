package com.example.rpccore.tcp;

import cn.hutool.core.util.IdUtil;
import com.example.rpccore.RpcApplication;
import com.example.rpccore.model.RpcRequest;
import com.example.rpccore.model.RpcResponse;
import com.example.rpccore.model.ServiceMetaInfo;
import com.example.rpccore.protocol.*;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetSocket;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Vertx请求客户端
 */
public class VertxTcpClient {

    /**
     * 发送请求
     * @param rpcRequest
     * @param serviceMetaInfo
     * @return
     */
    public static RpcResponse doRequest(RpcRequest rpcRequest, ServiceMetaInfo serviceMetaInfo) throws ExecutionException, InterruptedException {
        //发送TCP请求
        Vertx vertx = Vertx.vertx();
        NetClient netClient = vertx.createNetClient();
        CompletableFuture<RpcResponse> responseCompletableFuture = new CompletableFuture<>();
        netClient.connect(serviceMetaInfo.getServicePort(),serviceMetaInfo.getServiceHost(),
        result -> {
            if(!result.succeeded()) {
                System.err.println("Failed to connect to TCP server");
                return;
            }
            NetSocket socket = result.result();
            //发送数据
            //构造消息
            ProtocolMessage<RpcRequest> protocolMessage = new ProtocolMessage<>();
            ProtocolMessage.Header header = new ProtocolMessage.Header();
            header.setMagic(ProtocolConstant.PROTOCOL_MAGIC);
            header.setVersion(ProtocolConstant.PROTOCOL_VERSION);
            header.setSerializer((byte) ProtocolMessageSerializerEnum.getSerializerEnumByValue(
                    RpcApplication.getRpcConfig().getSerializerKey()
            ).getKey());
            header.setType((byte) ProtocolMessageTypeEnum.REQUEST.getKey());
            //生成全局请求ID
            header.setRequestId(IdUtil.getSnowflakeNextId());
            protocolMessage.setHeader(header);
            protocolMessage.setBody(rpcRequest);

            //编码请求
            try {
                Buffer encode = ProtocolMessageEncoder.encode(protocolMessage);
                socket.write(encode);
            } catch (IOException e) {
                throw new RuntimeException("协议消息编码错误");
            }

            //接受响应
            TcpBufferHandlerWrapper bufferHandlerWrapper = new TcpBufferHandlerWrapper(
                    buffer -> {
                        try {
                            ProtocolMessage<RpcResponse> rpcResponseProtocolMessage =
                                    (ProtocolMessage<RpcResponse>) ProtocolMessageDecoder.decode(buffer);
                            responseCompletableFuture.complete(rpcResponseProtocolMessage.getBody());
                        } catch (IOException e) {
                            throw new RuntimeException("协议消息解码错误");
                        }
                    }
            );

            socket.handler(bufferHandlerWrapper);
        });

        RpcResponse rpcResponse = responseCompletableFuture.get();
        //关闭连接
        netClient.close();
        return rpcResponse;
    }

    public void start() {
        //创建Vert.x实例
        Vertx vertx = Vertx.vertx();

        vertx.createNetClient().connect(8888,"localhost",result -> {
            if(result.succeeded()) {
                System.out.println("Connected to TCP server");
                NetSocket socket = result.result();
                for (int i = 0; i < 1000; i++) {
                    //发送数据
//                    socket.write("Hello, jingliu! Hello, jingliu! Hello, jingliu! Hello jingliu!");
                    //发送数据
                    Buffer buffer = Buffer.buffer();
                    String str = "Hello, jingliu! Hello, jingliu! Hello, jingliu! Hello jingliu!";
                    buffer.appendInt(0);
                    buffer.appendInt(str.getBytes().length);
                    buffer.appendBytes(str.getBytes());
                    socket.write(buffer);
                }
                //接收响应
                socket.handler(buffer -> {
                    System.out.println("Received response from server: " + buffer.toString());
                });
            } else {
                System.err.println("Failed to connect to TCP server");
            }
        });
    }

    public static void main(String[] args) {
        new VertxTcpClient().start();
    }
}
