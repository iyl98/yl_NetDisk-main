package com.yl.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
//用户信息
public class User {
    private int userId; //用户ID
    private String username;   //用户名
    private String password;    //密码
    private String nickname;    //昵称
    private String realname;    //真实姓名
    private int gender;     //性别
    private String phone;   //手机号
    private String email;   //邮箱
    private String info;    //个人简介
    private int level; //等级
    private int isVip;  //会员等级
    private long memorySize;    //总容量
    private long userSize;    //使用容量
    private int privateStatus;  //私密状态
    private String privatePass; //

}
