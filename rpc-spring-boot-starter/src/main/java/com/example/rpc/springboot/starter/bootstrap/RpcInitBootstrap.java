package com.example.rpc.springboot.starter.bootstrap;

import com.example.rpc.springboot.starter.annotation.EnableRpc;
import com.example.rpccore.RpcApplication;
import com.example.rpccore.config.RpcConfig;
import com.example.rpccore.tcp.VertexTcpServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * RPC框架启动
 */
@Slf4j
public class RpcInitBootstrap implements ImportBeanDefinitionRegistrar {
    /**
     * Spring初始化时执行，初始化RPC框架
     * @param importingClassMetadata
     * @param registry
     */
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata,
                                        BeanDefinitionRegistry registry) {
        //获取EnableRpc注解的属性值
        boolean needServer = (boolean) importingClassMetadata
                .getAnnotationAttributes(EnableRpc.class.getName()).get("needServer");
        //RPC框架初始化（配置和注册中心）
        RpcApplication.init();

        //全局配置
        final RpcConfig rpcConfig = RpcApplication.getRpcConfig();

        //启动服务器
        if(needServer) {
            VertexTcpServer vertexTcpServer = new VertexTcpServer();
            vertexTcpServer.doStart(rpcConfig.getServerPort());
        } else {
            log.info("不启动server");
        }
    }
}
