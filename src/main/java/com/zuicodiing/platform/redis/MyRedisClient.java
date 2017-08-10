package com.zuicodiing.platform.redis;

import com.zuicodiing.platform.redis.command.Command;
import com.zuicodiing.platform.redis.constant.Constant;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

/**
 * Created by Stephen.lin on 2017/8/10.
 * <p>
 * Description :<p>自己实现redis客户端</p>
 */
public class MyRedisClient {


    private InputStream reader;
    private OutputStream writer;

    private Socket socket;

    public MyRedisClient() throws IOException {
        this("127.0.0.1",6379);
    }



    public MyRedisClient(String ip) throws IOException {
        this(ip,6379);
    }

    public MyRedisClient(String ip, int port) throws IOException {
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
        command.append(Command.SET.buildCommand());
        command.append("$").append(key.getBytes("UTF-8").length).append(Constant.END_LINE);
        command.append(key).append(Constant.END_LINE);
        command.append("$").append(value.getBytes("UTF-8").length).append(Constant.END_LINE);
        command.append(value).append(Constant.END_LINE);

        sendCommand(command);

        return response();

    }

    private void sendCommand(StringBuilder command) throws IOException {
        writer.write(command.toString().getBytes("UTF-8") );
        writer.flush();
    }

    public String slaveOf(String server,int port) throws IOException {
        StringBuilder command = new StringBuilder();
        command.append(Command.SLAVEOF.buildCommand());
        command.append("$").append(server.getBytes().length).append(Constant.END_LINE);
        command.append(server).append(Constant.END_LINE);
        //command.append("$").append(String.valueOf(port).getBytes().length).append(Constant.END_LINE);
        command.append("$").append(String.valueOf(port).getBytes().length).append(Constant.END_LINE);
        command.append(port).append(Constant.END_LINE);
        System.err.println(command.toString());
        sendCommand(command);

        return response();
    }

    private String response() throws IOException {
        byte[] buffer = new byte[8192];
        int limit = reader.read(buffer);

        return new String(buffer,0,limit);
    }

    public String get(String key) throws IOException {
        StringBuilder command = new StringBuilder();
        command.append(Command.GET.buildCommand());
        command.append("$").append(key.getBytes("UTF-8").length).append(Constant.END_LINE);
        command.append(key).append(Constant.END_LINE);
        sendCommand(command);
        return response();
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
       // new MyRedisClient("127.0.0.1").getSubscripe().topic("demo");
       MyRedisClient client = new MyRedisClient();
       // System.err.println("set command:" + client.set("test-1","test"));
        //System.err.println("get command:"+client.get("12345678901"));

        System.err.println(client.slaveOf("127.0.0.1",6379));
    }
}
