package com.example.rpccore;

import com.example.rpccore.loadbalancer.LoadBalancer;
import com.example.rpccore.loadbalancer.RoundRobinLoadBalancer;
import com.example.rpccore.model.ServiceMetaInfo;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 负载均衡器测试
 */
public class LoadBalancerTest {
    final LoadBalancer loadBalancer = new RoundRobinLoadBalancer();

    @Test
    public void testLoadBalancer() {
        Map<String,Object> requestParams = new HashMap<>();
        requestParams.put("methodName","apple");
        ServiceMetaInfo serviceMetaInfo1 = new ServiceMetaInfo();
        serviceMetaInfo1.setServiceHost("hhhh");
        serviceMetaInfo1.setServiceVersion("1.0");
        serviceMetaInfo1.setServiceName("jingliuService");
        serviceMetaInfo1.setServicePort(8080);
        ServiceMetaInfo serviceMetaInfo2 = new ServiceMetaInfo();
        serviceMetaInfo1.setServiceHost("lian-ymy");
        serviceMetaInfo1.setServiceVersion("1.0");
        serviceMetaInfo1.setServiceName("kafukaService");
        serviceMetaInfo1.setServicePort(80);
        List<ServiceMetaInfo> serviceMetaInfoList = Arrays.asList(serviceMetaInfo1,serviceMetaInfo2);
        //连续调用三次查看效果
        ServiceMetaInfo serviceMetaInfo = loadBalancer.select(requestParams, serviceMetaInfoList);
        System.out.println(serviceMetaInfo);
        Assert.assertNotNull(serviceMetaInfo);
        serviceMetaInfo = loadBalancer.select(requestParams,serviceMetaInfoList);
        System.out.println(serviceMetaInfo);
        Assert.assertNotNull(serviceMetaInfo);
        serviceMetaInfo = loadBalancer.select(requestParams,serviceMetaInfoList);
        System.out.println(serviceMetaInfo);
        Assert.assertNotNull(serviceMetaInfo);
    }
}
