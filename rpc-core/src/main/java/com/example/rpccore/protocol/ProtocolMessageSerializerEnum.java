package com.example.rpccore.protocol;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 协议消息的序列化器枚举
 */
@Getter
public enum ProtocolMessageSerializerEnum {
    JDK(0,"jdk"),
    JSON(1,"json"),
    KRYO(2,"kryo"),
    HESSIAN(3,"hessian");

    private final int key;

    private final String value;

    ProtocolMessageSerializerEnum(int key, String value) {
        this.key = key;
        this.value = value;
    }

    /**
     * 获取值列表
     * @return
     */
    public static List<String> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    /**
     * 根据key值获取枚举类型
     * @param key
     * @return
     */
    public static ProtocolMessageSerializerEnum getSerializerEnumByKey(int key) {
        for (ProtocolMessageSerializerEnum anEnum : ProtocolMessageSerializerEnum.values()) {
            if(anEnum.key == key) {
                return anEnum;
            }
        }
        return null;
    }

    /**
     * 根据value值获取枚举类型
     * @param value
     * @return
     */
    public static ProtocolMessageSerializerEnum getSerializerEnumByValue(String value) {
        if(value == null) {
            return null;
        }
        for (ProtocolMessageSerializerEnum anEnum : ProtocolMessageSerializerEnum.values()) {
            if(anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }
}
