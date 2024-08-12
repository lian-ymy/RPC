package com.example.rpccore.config;

import com.example.rpccore.fault.retry.RetryStrategyKeys;
import com.example.rpccore.fault.tolerant.TolerantStrategyKeys;
import com.example.rpccore.loadbalancer.LoadBalancerKeys;
import com.example.rpccore.serializer.SerializerKeys;
import lombok.Data;

/**
 * RPC配置类
 */
@Data
public class RpcConfig {
    /**
     * 序列化器
     */
    private String serializerKey = SerializerKeys.JDK;

    /**
     * 名称
     */
    private String name = "ymy-rpc";

    /**
     * 版本号
     */
    private String version = "1.0";

    /**
     * 服务器主机名
     */
    private String serverHost = "localhost";

    /**
     * 服务器端口号
     */
    private Integer serverPort = 8080;

    /**
     * 模拟调用
     */
    private boolean mock = false;

    /**
     * 注册中心
     */
    private RegistryConfig registryConfig = new RegistryConfig();

    /**
     * 负载均衡器
     */
    private String loadBalancer = LoadBalancerKeys.ROUND_ROBIN;

    /**
     * 重试策略
     */
    private String retryStrategy = RetryStrategyKeys.NO;

    /**
     * 容错机制
     */
    private String tolerantStrategy = TolerantStrategyKeys.FAIL_FAST;
}
