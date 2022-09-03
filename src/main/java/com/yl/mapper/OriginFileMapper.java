package com.yl.mapper;

import com.yl.pojo.OriginFile;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface OriginFileMapper {
    OriginFile queryById(int originFileId);
    int addOriginFile(OriginFile originFile);
    OriginFile queryByURL(String fileUrl);
    OriginFile queryByMD5(String fileMd5);
    int updateOriginFile(OriginFile originFile);
}
