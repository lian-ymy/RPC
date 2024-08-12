package com.example.rpccore.proxy;

import cn.hutool.core.collection.CollectionUtil;
import com.example.rpccore.RpcApplication;
import com.example.rpccore.config.RpcConfig;
import com.example.rpccore.constant.RpcConstant;
import com.example.rpccore.fault.retry.RetryStrategy;
import com.example.rpccore.fault.retry.RetryStrategyFactory;
import com.example.rpccore.fault.tolerant.TolerantStrategy;
import com.example.rpccore.fault.tolerant.TolerantStrategyFactory;
import com.example.rpccore.loadbalancer.LoadBalancer;
import com.example.rpccore.loadbalancer.LoadBalancerFactory;
import com.example.rpccore.model.RpcRequest;
import com.example.rpccore.model.RpcResponse;
import com.example.rpccore.model.ServiceMetaInfo;
import com.example.rpccore.registry.Registry;
import com.example.rpccore.registry.RegistryFactory;
import com.example.rpccore.serializer.Serializer;
import com.example.rpccore.serializer.SerializerFactory;
import com.example.rpccore.tcp.VertxTcpClient;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

/**
 * 动态代理（JDK动态代理）
 */
public class ServiceProxy implements InvocationHandler {
    /**
     * 调用代理
     * @param proxy the proxy instance that the method was invoked on
     *
     * @param method the {@code Method} instance corresponding to
     * the interface method invoked on the proxy instance.  The declaring
     * class of the {@code Method} object will be the interface that
     * the method was declared in, which may be a superinterface of the
     * proxy interface that the proxy class inherits the method through.
     *
     * @param args an array of objects containing the values of the
     * arguments passed in the method invocation on the proxy instance,
     * or {@code null} if interface method takes no arguments.
     * Arguments of primitive types are wrapped in instances of the
     * appropriate primitive wrapper class, such as
     * {@code java.lang.Integer} or {@code java.lang.Boolean}.
     *
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args){
        //指定序列化器
        final Serializer serializer =
                SerializerFactory.getInstance(RpcApplication.getRpcConfig().getSerializerKey());

        //构造请求
        String serviceName = method.getDeclaringClass().getName();
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(serviceName)
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .args(args)
                .build();

        //从注册中心获取服务提供者请求地址
        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        Registry registry = RegistryFactory.getInstance(rpcConfig.getRegistryConfig().getRegistry());
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName(serviceName);
        serviceMetaInfo.setServiceVersion(RpcConstant.DEFAULT_SERVICE_VERSION);
        List<ServiceMetaInfo> serviceMetaInfoList = registry.serviceDiscovery(serviceMetaInfo.getServiceKey());
        if(CollectionUtil.isEmpty(serviceMetaInfoList)) {
            throw new RuntimeException("暂无服务地址");
        }
        //负载均衡
        LoadBalancer loadBalancer = LoadBalancerFactory.getInstance(rpcConfig.getLoadBalancer());
        //将调用方法名（请求路径）作为负载均衡参数
        HashMap<String, Object> requestParams = new HashMap<>();
        ServiceMetaInfo selectedServiceMetaInfo = loadBalancer.select(requestParams, serviceMetaInfoList);

        //发送RPC请求
        RpcResponse rpcResponse;
        try {
            RetryStrategy retryStrategy = RetryStrategyFactory
                    .getInstance(rpcConfig.getRetryStrategy());
            rpcResponse = retryStrategy
                    .doRetry(() -> VertxTcpClient.doRequest(rpcRequest, selectedServiceMetaInfo));
        } catch (Exception e) {
            TolerantStrategy tolerantStrategy = TolerantStrategyFactory
                    .getInstance(rpcConfig.getTolerantStrategy());
            rpcResponse = tolerantStrategy.doTolerant(null,e);
        }
        return rpcResponse.getData();
    }
}




