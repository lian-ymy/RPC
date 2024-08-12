package com.example.rpccore.bootstrap;

import com.example.rpccore.RpcApplication;
import com.example.rpccore.config.RegistryConfig;
import com.example.rpccore.config.RpcConfig;
import com.example.rpccore.model.ServiceMetaInfo;
import com.example.rpccore.model.ServiceRegisterInfo;
import com.example.rpccore.registry.LocalRegistry;
import com.example.rpccore.registry.Registry;
import com.example.rpccore.registry.RegistryFactory;
import com.example.rpccore.tcp.VertexTcpServer;

import java.util.List;

/**
 * 服务注册方法提供类
 */
public class ProviderBootstrap {
    public static void init(List<ServiceRegisterInfo> serviceRegisterInfoList) {
        //RPC框架初始化（配置和注册中心）
        RpcApplication.init();
        //全局配置
        final RpcConfig rpcConfig = RpcApplication.getRpcConfig();

        //注册服务
        for (ServiceRegisterInfo<?> serviceRegisterInfo : serviceRegisterInfoList) {
            String serviceName = serviceRegisterInfo.getServiceName();
            //本地注册
            LocalRegistry.register(serviceName,serviceRegisterInfo.getImplClass());

            //注册服务到注册中心
            RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
            Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceName);
            serviceMetaInfo.setServiceHost(rpcConfig.getServerHost());
            serviceMetaInfo.setServicePort(rpcConfig.getServerPort());
            try {
                registry.register(serviceMetaInfo);
            } catch (Exception e) {
                throw new RuntimeException(serviceName + "服务注册失败",e);
            }
        }

//        VertxHttpServer vertxHttpServer = new VertxHttpServer();
//        vertxHttpServer.doStart(rpcConfig.getServerPort());
        VertexTcpServer vertexTcpServer = new VertexTcpServer();
        vertexTcpServer.doStart(rpcConfig.getServerPort());
    }
}
