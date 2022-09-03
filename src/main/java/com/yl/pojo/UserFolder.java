package com.yl.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
//用户文件夹
public class UserFolder {
    private int folderId;   //文件夹ID
    private int userId; //用户ID
    private int parentId;   //父级ID
    private String folderName;  //文件夹名
    private int folderStatus;   //文件夹状态
    private Date createTime;    //创建时间
    private Date modifyTime;    //更新时间
    private Date deleteTime;    //删除时间
}
