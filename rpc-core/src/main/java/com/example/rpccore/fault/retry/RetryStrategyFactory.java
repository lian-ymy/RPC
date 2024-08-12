package com.example.rpccore.fault.retry;

import com.example.rpccore.spi.SpiLoader;

/**
 * 重试策略工厂
 */
public class RetryStrategyFactory {
    static {
        SpiLoader.load(RetryStrategy.class);
    }

    /**
     * 默认重试策略
     */
    private final static RetryStrategy DEFAULT_RETRY_STRATEGY = new NoRetryStrategy();

    public static RetryStrategy getInstance(String key) {
        return SpiLoader.getInstance(RetryStrategy.class,key);
    }
}
