package com.example.consumer;

import com.example.common.model.User;
import com.example.common.service.UserService;
import com.example.rpccore.proxy.ServiceProxyFactory;
import com.example.rpccore.serializer.JdkSerializer;
import com.example.rpccore.spi.SpiLoader;

/**
 * 简易服务消费者实例
 */
public class EasyConsumerExample {
    public static void main(String[] args) {
        //静态代理
//        UserServiceProxy userServiceProxy = new UserServiceProxy();

        SpiLoader.load(JdkSerializer.class);
        //动态代理
        UserService userService = ServiceProxyFactory.getProxy(UserService.class);

        User user = new User();
        user.setName("jingliu");
        User newUser = userService.getUser(user);
        // 调用
        if (newUser != null) {
            System.out.println(newUser.getName());
        } else {
            System.out.println("user == null");
        }
        int number = userService.getNumber();
        System.out.println(number);
    }
}
