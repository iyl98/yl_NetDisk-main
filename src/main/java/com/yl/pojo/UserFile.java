package com.yl.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
//用户文件
public class UserFile {
    private int fileId; //文件ID
    private int userId; //用户ID
    private int parentId;   //父级ID
    private int originId; //根ID
    private String fileName;    //文件名
    private Long fileSize;  //文件大小
    private String fileType;    //文件类型
    private int fileStatus; //文件状态
    private Date createTime;    //创建时间
    private Date modifyTime;    //更新时间
    private Date deleteTime;    //删除时间

    public UserFile(int userId, int parentId, int originId, String fileName, Long fileSize, String fileType, int fileStatus, Date createTime, Date modifyTime, Date deleteTime) {
        this.userId = userId;
        this.parentId = parentId;
        this.originId = originId;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.fileType = fileType;
        this.fileStatus = fileStatus;
        this.createTime = createTime;
        this.modifyTime = modifyTime;
        this.deleteTime = deleteTime;
    }
}
