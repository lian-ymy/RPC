package com.example.rpccore.fault.retry;

import com.example.rpccore.model.RpcResponse;

import java.util.concurrent.Callable;

/**
 * 不重试策略实现
 */
public class NoRetryStrategy implements RetryStrategy{
    @Override
    public RpcResponse doRetry(Callable<RpcResponse> callable) throws Exception {
        return callable.call();
    }
}
