package com.example.consumer;

import com.example.common.model.User;
import com.example.common.service.UserService;
import com.example.rpccore.bootstrap.ConsumerBootstrap;
import com.example.rpccore.proxy.ServiceProxyFactory;

/**
 * 简易服务消费者实例
 */
public class ConsumerExample {
    public static void main(String[] args) {
        //服务提供者初始化
        ConsumerBootstrap.init();

        //获取代理
        UserService userService = ServiceProxyFactory.getProxy(UserService.class);
        User user = new User();
        user.setName("jingliu");
        //调用
        User newUser = userService.getUser(user);
        if(newUser != null) {
            System.out.println(newUser.getName());
        } else {
            System.out.println("user == null");
        }
    }
}
