package com.event.utils;
import java.text.DecimalFormat;

public class Uuid {

    public static String getUUID()
    {
        java.util.Random r=new java.util.Random();
        //如生成的随机位数不足6位则自动加零补充
        DecimalFormat g=new DecimalFormat("1000000");
        //返回时间增量+随机数的序列
        return String.format("%s%s",System.currentTimeMillis(),g.format(r.nextInt(1000000)));
    }
}
