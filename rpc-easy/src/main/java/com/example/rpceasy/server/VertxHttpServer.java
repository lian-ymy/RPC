package com.example.rpceasy.server;

import com.example.rpceasy.model.HttpServerHandler;
import io.vertx.core.Vertx;

public class VertxHttpServer implements HttpServer{
    /**
     * 启动服务器
     * @param port
     */
    @Override
    public void doStart(int port) {
        //创建Vert.x实例
        Vertx vertx = Vertx.vertx();

        //创建HTTP服务器
        io.vertx.core.http.HttpServer server = vertx.createHttpServer();

        //监听端口并处理请求
//        server.requestHandler(httpServerRequest -> {
//            //处理HTTP请求
//            System.out.println("Received request: "+httpServerRequest.method()+" " + httpServerRequest.uri());
//
//            //发送HTTP响应
//            httpServerRequest.response()
//                    .putHeader("content-type", "text-plain")
//                    .end("Hello from Vert.x HTTP server, 镜流给你打招呼呢！");
//        });
        server.requestHandler(new HttpServerHandler());

        //启动HTTP服务器并监听指定端口
        server.listen(port, httpServerAsyncResult -> {
            if(httpServerAsyncResult.succeeded()) {
                System.out.println("Server is now listening on port" + port);
            } else {
                System.out.println("Failed to start server: " + httpServerAsyncResult.cause());
            }
        });
    }
}
