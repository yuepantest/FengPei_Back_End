package com.fengpei.web.tool;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class FormalTool {
    public String setStringDoubledEnd2(double originalValue) {
        return String.format("%.2f", originalValue);
    }

    public boolean judeListHasNull(Object... origins) {
        for (Object str : origins) {
            if (str == null) {
                return true;
            }
        }
        return false;
    }

    public String getCurrentTime() {
        // 获取当前日期时间
        LocalDateTime now = LocalDateTime.now();
        // 定义日期时间格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return now.format(formatter);
    }

    public String getReturnTime() {
        // 获取当前日期时间
        LocalDateTime now = LocalDateTime.now();
        long time = System.currentTimeMillis();
        // 定义日期时间格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMddHHmm");
        String format = now.format(formatter);
        return time+"";
    }

    public String getRandom4() {
        Random random = new Random();
        int randomNumber = random.nextInt(9000) + 1000; // 生成1000-9999的随机数
        return String.format("%04d", randomNumber); // 格式化为四位数
    }

    public String subString(String content,int start, int end) {
        return content.substring(start, end);
    }
}
