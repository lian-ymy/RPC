package com.example.provider;

import com.example.common.service.UserService;
import com.example.rpccore.RpcApplication;
import com.example.rpccore.registry.LocalRegistry;
import com.example.rpccore.server.HttpServer;
import com.example.rpccore.server.VertxHttpServer;

/**
 * 简易服务提供者示例
 */
public class EasyProviderExample {
    public static void main(String[] args) {
        //RPC框架初始化
        RpcApplication.init();

        //注册服务
        LocalRegistry.register(UserService.class.getName(),UserServiceImpl.class);

        //启动web服务
        HttpServer httpServer = new VertxHttpServer();
        httpServer.doStart(RpcApplication.getRpcConfig().getServerPort());
    }
}
