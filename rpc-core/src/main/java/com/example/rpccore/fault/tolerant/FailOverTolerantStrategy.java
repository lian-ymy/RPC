package com.example.rpccore.fault.tolerant;

import com.example.rpccore.model.RpcResponse;

import java.util.Map;

/**
 * 转移到其他服务策略
 */
public class FailOverTolerantStrategy implements TolerantStrategy{
    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) {
        //todo 实现服务转移策略容错机制
        return new RpcResponse();
    }
}
