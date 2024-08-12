package com.example.consumer;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.example.common.model.User;
import com.example.common.service.UserService;
import com.example.rpccore.model.RpcRequest;
import com.example.rpccore.model.RpcResponse;
import com.example.rpccore.serializer.JdkSerializer;
import com.example.rpccore.serializer.Serializer;

import java.io.IOException;
import java.util.List;

/**
 * 静态代理
 *
 * 构造http服务器去调用服务提供者，一次只能针对一个方法进行代理，
 * 如果有多个方法需要写大量重复冗余代码
 */
public class UserServiceProxy implements UserService{
    public User getUser(User user) {
        //指定序列化器
        Serializer serializer = new JdkSerializer();

        //发请求
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(UserService.class.getName())
                .methodName("getName")
                .parameterTypes(new Class[]{User.class})
                .args(new Object[]{user})
                .build();
        try {
            byte[] bodyBytes = serializer.serialize(rpcRequest);
            byte[] result;
            //todo 一个没改的地方
            HttpResponse httpResponse = HttpRequest.post("http://localhost:8080")
                    .body(bodyBytes)
                    .execute();
            result = httpResponse.bodyBytes();
            RpcResponse rpcResponse = serializer.deserialize(bodyBytes, RpcResponse.class);
            return (User) rpcResponse.getData();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
