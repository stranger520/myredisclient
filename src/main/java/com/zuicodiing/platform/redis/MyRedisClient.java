package com.zuicodiing.platform.redis;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by Stephen.lin on 2017/8/10.
 * <p>
 * Description :<p>自己实现redis客户端</p>
 */
public class MyRedisClient {

    private String ip;
    private int port = 6379;

    private InputStream reader;
    private OutputStream writer;

    private Socket socket;
    public MyRedisClient(String ip) throws IOException {
        this.ip = ip;

        socket = new Socket(ip,port);

        reader = socket.getInputStream();
        writer = socket.getOutputStream();

    }

    /**
     * <code>
     *     //SET hello world
     *    *3  // 代表这个命令的协议 参数 是3个
     *    $3  // 代表第一个参数 string 类型，长度是3
     *    SET // 带表第一个参数值
     *    $5  //带表 第二个参数 是String 类型，长度是5
     *    hello // 带表第二个参数值
     *    $5  // 带表第三个参数 ，是string类型，长度是5
     *    world //带表第三个参数的值。
     * </code>
     * @param key
     * @param value
     * @return
     */
    public String set(String key, String value) throws IOException {
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
        byte[] buffer = new byte[512];
        reader.read(buffer);
        return new String(buffer);

    }

    /**
     * 获取管道
     * @return
     */
    public Pipline getPipline(){

        return new Pipline(reader,writer);
    }

    public Subscripe getSubscripe(){

        return new Subscripe(reader,writer);
    }



    public static void main(String[] args) throws IOException {
       //System.err.println( new MyRedisClient("127.0.0.1").set("hello","张三"));
        new MyRedisClient("127.0.0.1").getSubscripe().topic("demo");
    }
}
