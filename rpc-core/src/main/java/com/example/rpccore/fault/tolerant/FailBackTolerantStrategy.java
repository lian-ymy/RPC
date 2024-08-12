package com.example.rpccore.fault.tolerant;

import com.example.rpccore.model.RpcResponse;

import java.util.Map;

/**
 * 降级到其他策略（转移到底层服务）
 */
public class FailBackTolerantStrategy implements TolerantStrategy{
    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) {
        //todo 实现故障回溯功能
        return new RpcResponse();
    }
}
