package com.yl.mapper;

import com.yl.pojo.UserFile;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserFileMapper {
    List<UserFile> queryByUserId(int userId);
    List<UserFile> queryByUserIdAll(int userId);
    UserFile queryByFileId(int fileId);
    List<UserFile> queryByParentId(int userId, int parentId);
    List<UserFile> queryByFileName(int userId, String fileName);
    List<UserFile> queryByFileTypePhoto(int userId);
    List<UserFile> queryByFileTypeDocument(int userId);
    List<UserFile> queryByFileTypeVideo(int userId);
    List<UserFile> queryByFileTypeMusic(int userId);
    List<UserFile> queryByFileTypeCompressedFile(int userId);
    List<UserFile> queryRecycleFileByUserId(int userId);
    Integer queryOriginIdByFileId(Integer fileId);
    int addFile(UserFile userFile);
    int updateFile(UserFile userFile);

}
