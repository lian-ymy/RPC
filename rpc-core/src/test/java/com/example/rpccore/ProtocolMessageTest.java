package com.example.rpccore;

import cn.hutool.core.util.IdUtil;
import com.example.rpccore.constant.RpcConstant;
import com.example.rpccore.model.RpcRequest;
import com.example.rpccore.protocol.*;
import io.vertx.core.buffer.Buffer;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class ProtocolMessageTest {
    @Test
    public void testEncodeAndDecode() throws IOException {
        //构造消息
        ProtocolMessage<RpcRequest> protocolMessage = new ProtocolMessage<>();
        ProtocolMessage.Header header = new ProtocolMessage.Header();
        header.setMagic((byte) ProtocolConstant.PROTOCOL_MAGIC);
        header.setVersion((byte)ProtocolConstant.PROTOCOL_VERSION);
        header.setSerializer((byte) ProtocolMessageSerializerEnum.JDK.getKey());
        header.setType((byte) ProtocolMessageTypeEnum.REQUEST.getKey());
        header.setStatus((byte) ProtocolMessageStatusEnum.OK.getValue());
        header.setRequestId(IdUtil.getSnowflakeNextId());
        header.setBodyLength(0);
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setServiceName("jingliuService");
        rpcRequest.setMethodName("jingMethod");
        rpcRequest.setServiceVersion(RpcConstant.DEFAULT_SERVICE_VERSION);
        rpcRequest.setParameterTypes(new Class[]{String.class});
        rpcRequest.setArgs(new Object[]{"kafuka","heitiane"});
        protocolMessage.setBody(rpcRequest);
        protocolMessage.setHeader(header);

        Buffer encode = ProtocolMessageEncoder.encode(protocolMessage);
        ProtocolMessage<?> message = ProtocolMessageDecoder.decode(encode);
        System.out.println(message);
        Assert.assertNotNull(message);
    }
}
