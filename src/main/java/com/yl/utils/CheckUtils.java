package com.yl.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckUtils {
    public static boolean isChinaPhoneLegal(String str){
        String regExp = "^((13[0-9])|(15[^4])|(18[0,2,3,5-9])|(17[0-8])|(147))\\d{8}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    //验证用户名，必须是6-10位字母、数字、下划线，且开头不能是数字
    public static boolean checkName(String username){
        String regExp = "^[^0-9][\\w_]{5,9}$";
        if(username.matches(regExp)){
            return true;
        } else {
            return false;
        }
    }

    //验证密码必须是6-20位且是数字、字母、下划线任意两个组合
    public static boolean checkPwd(String pwd){
        String regExp = "^[\\w_]{6,20}$";
        if(pwd.matches(regExp)){
            return true;
        }
        return false;
    }
}
