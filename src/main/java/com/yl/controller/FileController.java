package com.yl.controller;

import com.yl.mapper.NoticeUpdateMapper;
import com.yl.mapper.UserFileMapper;
import com.yl.mapper.UserFolderMapper;
import com.yl.pojo.NoticeUpdate;
import com.yl.pojo.User;
import com.yl.pojo.UserFile;
import com.yl.pojo.UserFolder;
import com.yl.utils.FileSizeHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class FileController {
    @Autowired
    UserFolderMapper userFolderMapper;
    @Autowired
    UserFileMapper userFileMapper;
    @Autowired
    NoticeUpdateMapper noticeUpdateMapper;

    /**
     * 用户主页
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("/user/home")
    public String home(HttpSession session, Model model){
        User user = (User) session.getAttribute("user");

        //查找用户根目录文件夹
        List<UserFolder> userFolders = userFolderMapper.queryByUserId(user.getUserId());

        //查找用户根目录下文件
        List<UserFile> userFiles = userFileMapper.queryByUserId(user.getUserId());

        //判断当前目录下是否有文件，计算大小存入集合
        if(userFiles.size() >= 1){
            Map<Integer, String> fileSize = new HashMap<>();
            for (UserFile userfile: userFiles) {
                //查找文件大小并转换为适用的单位
                fileSize.put(userfile.getFileId(), FileSizeHelper.getHumanReadableFileSize(userfile.getFileSize()));
            }

            model.addAttribute("fileSize", fileSize);

        }

        model.addAttribute("userFiles", userFiles);

        model.addAttribute("userFolders", userFolders);

        session.setAttribute("uploadId", 0);

        return "user/home";
    }

    @RequestMapping("/user/notice")
    public String notice(Model model){
        List<NoticeUpdate> noticeUpdates = noticeUpdateMapper.queryAll();
        //!!!!!!!!!!!!!!!!!!!!!!
        List<NoticeUpdate> result = noticeUpdates.stream().sorted(Comparator.comparingInt(NoticeUpdate::getNoticeId).reversed()).collect(Collectors.toList());
        model.addAttribute("noticeUpdates", result);

        return "user/notice";

    }

    /**
     * 多级目录
     * @param parentId 当前id 同时是下一级文件的父级id
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("/user/home/{parentId}")
    public String deepPage(@PathVariable("parentId") int parentId, HttpSession session, Model model){
        User user = (User) session.getAttribute("user");
        //下载源父级id
        session.setAttribute("uploadId", parentId);
        List<UserFolder> userFolders = userFolderMapper.queryByParentId(user.getUserId(), parentId);
        List<UserFile> userFiles = userFileMapper.queryByParentId(user.getUserId(), parentId);
        model.addAttribute("userFiles",userFiles);
        model.addAttribute("userFolders",userFolders);
        if(userFiles.size() >= 1){
            Map<Integer, String> fileSize = new HashMap<>();
            for (UserFile userFile: userFiles) {
                fileSize.put(userFile.getFileId(),FileSizeHelper.getHumanReadableFileSize(userFile.getFileSize()));
            }
            model.addAttribute("fileSize", fileSize);
        }

        return "user/home";

    }

    /**
     * 多文件下载页面跳转
     * @return
     */
    @RequestMapping("/user/uploadPage")
    public String uploadPage(){
        return "user/uploadPage";
    }

    /**
     * 查找功能
     * @param fileName
     * @param session
     * @param model
     * @return
     */
    @GetMapping("/user/queryFileName")
    public String queryFileName(String fileName, HttpSession session, Model model){
        User user = (User) session.getAttribute("user");

        if(fileName==null || "".equals(fileName)){
            return "redirect:/user/home";
        }else {
            List<UserFolder> userFolders = userFolderMapper.queryByFolderName(user.getUserId(), fileName);
            List<UserFile> userFiles = userFileMapper.queryByFileName(user.getUserId(), fileName);
            if(userFiles.size() >= 1){
                Map<Integer, String> fileSize = new HashMap<>();
                for (UserFile userFile: userFiles) {
                    fileSize.put(userFile.getFileId(),FileSizeHelper.getHumanReadableFileSize(userFile.getFileSize()));
                }
                model.addAttribute("fileSize",fileSize);
            }

            model.addAttribute("userFiles",userFiles);
            model.addAttribute("userFolders",userFolders);
            return "user/home";
        }
    }

    /**
     * 管理所有分区页面
     * @return
     */
    @RequestMapping("/user/zones")
    public String zones(){return "/user/zones";}

    /**
     * 图片分区
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("/user/home/photos")
    public String queryPhotos(HttpSession session, Model model){
        User user = (User) session.getAttribute("user");
        List<UserFile> userFiles = userFileMapper.queryByFileTypePhoto(user.getUserId());
        model.addAttribute("userFiles",userFiles);
        if(userFiles.size() >= 1){
            Map<Integer, String> fileSize = new HashMap<>();
            for (UserFile userFile: userFiles) {
                fileSize.put(userFile.getFileId(), FileSizeHelper.getHumanReadableFileSize(userFile.getFileSize()));
            }
            model.addAttribute("fileSize",fileSize);
        }
        return "user/zones";
    }

    /**
     * 文档分区
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("/user/home/document")
    public String queryDocument(HttpSession session, Model model){
        User user = (User) session.getAttribute("user");
        List<UserFile> userFiles = userFileMapper.queryByFileTypeDocument(user.getUserId());
        model.addAttribute("userFiles",userFiles);
        if(userFiles.size() >= 1){
            Map<Integer, String> fileSize = new HashMap<>();
            for (UserFile userFile: userFiles) {
                fileSize.put(userFile.getFileId(), FileSizeHelper.getHumanReadableFileSize(userFile.getFileSize()));
            }
            model.addAttribute("fileSize",fileSize);
        }
        return "user/zones";
    }

    /**
     * 视频分区
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("/user/home/video")
    public String queryVideo(HttpSession session, Model model){
        User user = (User) session.getAttribute("user");
        List<UserFile> userFiles = userFileMapper.queryByFileTypeVideo(user.getUserId());
        model.addAttribute("userFiles",userFiles);
        if(userFiles.size() >= 1){
            Map<Integer, String> fileSize = new HashMap<>();
            for (UserFile userFile: userFiles) {
                fileSize.put(userFile.getFileId(), FileSizeHelper.getHumanReadableFileSize(userFile.getFileSize()));
            }
            model.addAttribute("fileSize",fileSize);
        }
        return "user/zones";
    }

    /**
     * 音乐分区
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("/user/home/music")
    public String queryMusic(HttpSession session, Model model){
        User user = (User) session.getAttribute("user");
        List<UserFile> userFiles = userFileMapper.queryByFileTypeMusic(user.getUserId());
        model.addAttribute("userFiles",userFiles);
        if(userFiles.size() >= 1){
            Map<Integer, String> fileSize = new HashMap<>();
            for (UserFile userFile: userFiles) {
                fileSize.put(userFile.getFileId(), FileSizeHelper.getHumanReadableFileSize(userFile.getFileSize()));
            }
            model.addAttribute("fileSize",fileSize);
        }
        return "user/zones";
    }

    /**
     * 压缩文件分区
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("/user/home/compressed")
    public String queryCompressedFile(HttpSession session, Model model){
        User user = (User) session.getAttribute("user");
        List<UserFile> userFiles = userFileMapper.queryByFileTypeCompressedFile(user.getUserId());
        model.addAttribute("userFiles",userFiles);
        if(userFiles.size() >= 1){
            Map<Integer, String> fileSize = new HashMap<>();
            for (UserFile userFile: userFiles) {
                fileSize.put(userFile.getFileId(), FileSizeHelper.getHumanReadableFileSize(userFile.getFileSize()));
            }
            model.addAttribute("fileSize",fileSize);
        }
        return "user/zones";
    }

    /**
     * 回收站分区
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("/user/home/recycle")
    public String queryRecycle(HttpSession session, Model model){
        User user = (User) session.getAttribute("user");
        List<UserFile> userFiles = userFileMapper.queryRecycleFileByUserId(user.getUserId());
        List<UserFolder> userFolders = userFolderMapper.queryRecycleFolderByUserId(user.getUserId());
        model.addAttribute("userFiles",userFiles);
        model.addAttribute("userFolders",userFolders);
        if(userFiles.size() >= 1){
            Map<Integer, String> fileSize = new HashMap<>();
            for (UserFile userFile: userFiles) {
                fileSize.put(userFile.getFileId(), FileSizeHelper.getHumanReadableFileSize(userFile.getFileSize()));
            }
            model.addAttribute("fileSize",fileSize);
        }
        return "user/recycle";
    }


}
