package com.example.rpccore.fault.tolerant;

import com.example.rpccore.model.RpcResponse;

import java.util.Map;

/**
 * 快速失败容错策略（出现异常后，立刻通知外层调用方）
 */
public class FailFastTolerantStrategy implements TolerantStrategy{
    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) {
        throw new RuntimeException("服务报错",e);
    }
}
