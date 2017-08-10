package com.zuicodiing.platform.redis;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Stephen.lin on 2017/8/10.
 * <p>
 * Description :<p></p>
 */
public class Subscripe {

    private InputStream reader;
    private OutputStream writer;

    public Subscripe(InputStream reader, OutputStream writer) {
        this.reader = reader;
        this.writer = writer;
    }

    /**
     * 订阅消息主题
     * @param topic
     */
    public void topic(String topic) throws IOException {
        StringBuilder command = new StringBuilder();
        command.append("*2").append("\r\n");
        command.append("$9").append("\r\n");
        command.append("SUBSCRIBE").append("\r\n");
        command.append("$").append(topic.getBytes().length).append("\r\n");
        command.append(topic).append("\r\n");
        writer.write(command.toString().getBytes());

        while (true){
            byte[] buffer = new byte[512];
            reader.read(buffer);
            System.err.println(new String(buffer));
        }
    }
}
