package com.yl.controller;

import com.yl.mapper.UserFolderMapper;
import com.yl.pojo.User;
import com.yl.pojo.UserFolder;
import com.yl.utils.GetNowUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Date;

@RestController
public class ChangeFileController {
    @Autowired
    UserFolderMapper userFolderMapper;

    //重命名
    @PostMapping("/user/rename")
    public Integer rename(HttpSession session, String fileName){
        if("".equals(fileName)){
            return 200;
        }

        try {
            UserFolder userFolder = new UserFolder();

            //获取时间
            Date parse = GetNowUtils.getNow();

            //获取用户id
            User user = (User) session.getAttribute("user");
            int userId = user.getUserId();

            //获取父级文件id
            Integer parentId = (Integer) session.getAttribute("uploadId");

            userFolder.setModifyTime(parse);
            userFolder.setFolderName(fileName);
            userFolder.setUserId(userId);
            userFolder.setParentId(parentId);
            Integer integer = userFolderMapper.updateFolder(userFolder);
            return 200;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 400;
    }

    //新建文件夹
    @PostMapping("/user/newFolder")
    public Integer newFolder(HttpSession session, String fileName) {
        if ("".equals(fileName)) {
            fileName = "新建文件夹";
        }

        try {
            UserFolder userFolder = new UserFolder();

            //获取时间
            Date parse = GetNowUtils.getNow();

            //获取用户id
            User user = (User) session.getAttribute("user");
            int userId = user.getUserId();

            //获取父级文件id
            Integer parentId = (Integer) session.getAttribute("uploadId");

            userFolder.setCreateTime(parse);
            userFolder.setModifyTime(parse);
            userFolder.setFolderName(fileName);
            userFolder.setUserId(userId);
            userFolder.setParentId(parentId);
            Integer integer = userFolderMapper.addNewFolder(userFolder);
            return 200;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 400;
    }
}
