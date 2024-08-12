package com.example.rpccore.loadbalancer;

import com.example.rpccore.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;

/**
 * 负载均衡器（消费端调用）
 */
public interface LoadBalancer {
    /**
     * 选择服务调用
     * @param requestParam
     * @param serviceMetaInfoList
     * @return
     */
    ServiceMetaInfo select(Map<String,Object> requestParam, List<ServiceMetaInfo> serviceMetaInfoList);
}
