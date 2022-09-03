package com.yl.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GetNowUtils {
    public static Date getNow(){
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String format = simpleDateFormat.format(new Date());
            return simpleDateFormat.parse(format);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
