package com.yl.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
//源文件信息
public class OriginFile {
    private int originFileId; //源文件ID
    private String fileMd5; //文件MD5
    private Long fileSize;   //文件大小
    private String fileType; //文件类型
    private String fileUrl;    //文件url
    private int fileCount;  //文件数量
    private int fileStatus;    //文件状态
    private Date createTime;    //创建时间
    private Date modifyTime;    //修改时间

    public OriginFile(String fileMd5, Long fileSize, String fileType, String fileUrl, int fileCount, int fileStatus, Date createTime, Date modifyTime) {
        this.fileMd5 = fileMd5;
        this.fileSize = fileSize;
        this.fileType = fileType;
        this.fileUrl = fileUrl;
        this.fileCount = fileCount;
        this.fileStatus = fileStatus;
        this.createTime = createTime;
        this.modifyTime = modifyTime;
    }
}
