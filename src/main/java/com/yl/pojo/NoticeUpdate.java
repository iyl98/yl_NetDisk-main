package com.yl.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoticeUpdate {
    private int noticeId;   //通知ID
    private Date noticeDate;    //通知日期
    private String noticeContent;   //通知内容
}
