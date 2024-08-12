package com.example.rpccore.fault.tolerant;

/**
 * 容错机制键值常量
 */
public interface TolerantStrategyKeys {
    /**
     * 静默处理
     */
    String FAIL_SAFE = "failSafe";

    /**
     * 快速失败
     */
    String FAIL_FAST = "failFast";

    /**
     * 故障降级
     */
    String FAIL_BACK = "failBack";

    /**
     * 故障转移
     */
    String FAIL_OVER = "failOver";
}
