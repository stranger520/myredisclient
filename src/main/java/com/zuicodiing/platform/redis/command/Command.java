package com.zuicodiing.platform.redis.command;

import com.zuicodiing.platform.redis.constant.Constant;

/**
 * Created by Stephen.lin on 2017/8/10.
 * <p>
 * Description :<p></p>
 */
public enum Command {
    SET("*3","$3","SET"),GET("*2","$3","GET"),SLAVEOF("*3","$7","SLAVEOF");

    private String numStr;
    private String paramStr;
    private String command;

    private Command(String numStr, String paramStr,String command) {
        this.numStr = numStr;
        this.paramStr = paramStr;
        this.command = command;
    }

    public String buildCommand(){
        StringBuilder command = new StringBuilder();
        command.append(getNumStr()).append(Constant.END_LINE);
        command.append(getParamStr()).append(Constant.END_LINE);
        command.append(getCommand()).append(Constant.END_LINE);
        return command.toString();
    }

    public String getNumStr() {
        return numStr;
    }

    public String getParamStr() {
        return paramStr;
    }

    public String getCommand() {
        return command;
    }

}
