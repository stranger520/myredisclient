package com.zuicodiing.platform.redis;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Stephen.lin on 2017/8/10.
 * <p>
 * Description :<p></p>
 */
public class Pipline {

    private InputStream reader;
    private OutputStream writer;

    public Pipline(InputStream reader, OutputStream writer) {
        this.reader = reader;
        this.writer = writer;
    }

    public void set(String key,String value) throws IOException {
        StringBuilder command = new StringBuilder();
        command.append("*3").append("\r\n");
        command.append("$3").append("\r\n");
        command.append("SET").append("\r\n");
        command.append("$").append(key.getBytes().length).append("\r\n");
        command.append(key).append("\r\n");
        command.append("$").append(value.getBytes().length).append("\r\n");
        command.append(value).append("\r\n");
        System.err.println(command.toString());
        writer.write(command.toString().getBytes());
        writer.flush();
    }

    /**
     * jedis pipline的原理就是利用，不用等着每次set 之后等返回值，利用管道就是最后获取返回值。
     * @return
     * @throws IOException
     */
    public String response() throws IOException {
        byte[] buffer = new byte[512];
        reader.read(buffer);
        return new String(buffer);
    }
}
