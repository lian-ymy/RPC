package com.example.common.service;

import com.example.common.model.User;

/**
 * 用户接口实现类
 */
public interface UserService {
    public User getUser(User user);

    /**
     * 测试mock方法默认实现类
     * @return
     */
    default int getNumber() {
        return 2;
    }
}
