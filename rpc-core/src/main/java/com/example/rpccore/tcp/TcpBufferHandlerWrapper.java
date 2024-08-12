package com.example.rpccore.tcp;

import com.example.rpccore.protocol.ProtocolConstant;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.parsetools.RecordParser;


/**
 * 装饰着模式（使用recordParser对原有的buffer处理能力进行增强）
 */
public class TcpBufferHandlerWrapper implements Handler<Buffer> {

    private final RecordParser recordParser;

    public TcpBufferHandlerWrapper(Handler<Buffer> bufferHandler) {
        recordParser = initRecodeParser(bufferHandler);
    }

    private RecordParser initRecodeParser(Handler<Buffer> bufferHandler) {
        //构造parser
        RecordParser parser = RecordParser.newFixed(ProtocolConstant.MESSAGE_HEADER_LENGTH);

        parser.setOutput(new Handler<Buffer>() {
            //初始化
            int size = -1;
            //一次完整的读取（头+体）
            io.vertx.core.buffer.Buffer resultBuffer = io.vertx.core.buffer.Buffer.buffer();

            @Override
            public void handle(io.vertx.core.buffer.Buffer buffer) {
                if(-1 == size) {
                    //读取消息体长度
                    size = buffer.getInt(13);
                    parser.fixedSizeMode(size);
                    //写入头信息到结果
                    resultBuffer.appendBuffer(buffer);
                } else {
                    //写入体信息到结果
                    resultBuffer.appendBuffer(buffer);
                    //已拼接为完整的Buffer，执行处理
                    bufferHandler.handle(resultBuffer);
                    //重置一轮
                    size = -1;
                    resultBuffer = Buffer.buffer();
                    parser.fixedSizeMode(ProtocolConstant.MESSAGE_HEADER_LENGTH);
                }
            }
        });
        return parser;
    }

    @Override
    public void handle(Buffer buffer) {
        recordParser.handle(buffer);
    }
}
