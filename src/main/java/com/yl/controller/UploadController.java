package com.yl.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.yl.mapper.OriginFileMapper;
import com.yl.mapper.UserFileMapper;
import com.yl.pojo.OriginFile;
import com.yl.pojo.User;
import com.yl.pojo.UserFile;
import com.yl.utils.GetFileMD5;
import com.yl.utils.GetNowUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
public class UploadController {
    @Autowired
    UserFileMapper userFileMapper;
    @Autowired
    OriginFileMapper originFileMapper;

    @Transactional
    @PostMapping("user/upload")
    @ResponseBody
    public Map<String, Object> upload(HttpServletRequest request, MultipartFile file, HttpSession session){
        Integer uploadId = (Integer) session.getAttribute("uploadId");
        User user = (User) session.getAttribute("user");

        Map<String, Object> result = new HashMap<>();

        Date now = GetNowUtils.getNow();
        if(file != null && !file.isEmpty()) {
            try {
                String filename = file.getOriginalFilename();

                //windows 需在tomcat的temp文件夹下创建files文件夹
                //String url = StrUtil.sub(request.getServletContext().getRealPath("/"), 0, 41) + "\\files\\" + IdUtil.randomUUID() + "=" + filename;
                //System.out.println(url);

                //linux
                //String url = "/tmp/files/"+filename;

                String url = "/tmp/files/" + IdUtil.randomUUID()+"="+filename;

                file.transferTo(new File(url));
                String md5 = GetFileMD5.getMD5Three(url);
                OriginFile queryByMD5 = originFileMapper.queryByMD5(md5);

                //若md5相同则源文件表数量+1
                //将源文件添加至用户文件表
                //删除上传的源文件并反馈

                if(queryByMD5 != null){
                    queryByMD5.setFileCount(queryByMD5.getFileCount()+1);

                    originFileMapper.updateOriginFile(queryByMD5);

                    //将源文件添加至用户文件表
                    UserFile userFile = new UserFile(user.getUserId(), uploadId, queryByMD5.getOriginFileId(), filename, queryByMD5.getFileSize(), queryByMD5.getFileType(), 1, now, now, now);
                    userFileMapper.addFile(userFile);

                    //删除上传的源文件并反馈
                    FileUtil.del(url);

                    result.put("code", 200);
                    result.put("msg", "success");

                    return result;
                }else {
                    //获取文件大小
                    Long fileSize = file.getSize();

                    //获取文件类型
                    String type = FileTypeUtil.getType(FileUtil.file(url));

                    //将上传的源文件写入数据库
                    OriginFile originFile = new OriginFile(GetFileMD5.getMD5Three(url), fileSize, type, url, 1, 1, now, now);
                    originFileMapper.addOriginFile(originFile);

                    //将上传文件的文件列表写入数据库
                    UserFile userFile = new UserFile(user.getUserId(),uploadId,originFileMapper.queryByURL(url).getOriginFileId(),filename,fileSize,type,1,now,now,now);
                    userFileMapper.addFile(userFile);
                }

                result.put("code", 200);
                result.put("msg", "success");
            } catch (IOException e) {
                result.put("code", -1);
                result.put("msg", "文件上传出错，请重试");
                e.printStackTrace();
                }
            } else {
                result.put("code", -1);
                result.put("msg", "未获取到有效文件信息，请重试");
            }
        return result;
    }

    /**
     * 多文件上传
     * @param file
     * @param session
     * @param request
     * @return
     */
    @Transactional
    @PostMapping(value = "/user/uploadFiles")
    @ResponseBody
    public String uploadSource(@RequestParam("file") MultipartFile file, HttpSession session, HttpServletRequest request){
        //获取父类文件id
        Integer uploadId = (Integer) session.getAttribute("uploadId");
        //获取用户信息
        User user = (User) session.getAttribute("user");
        //获取当前时间
        Date now = GetNowUtils.getNow();

        //创建下载路径
        String url = null;
        String filename = null;
        if(file!=null){
            //获取上传文件名称
            filename = file.getOriginalFilename();

            //谷歌浏览器获取直接文件名称
            //IE/Edge获取文件上传时完整路径、文件名

            int unixSep = filename.lastIndexOf("/");

            int winSep = filename.lastIndexOf("\\");

            int pos = (winSep > unixSep ? winSep:unixSep);
            if(pos != -1){
                filename = filename.substring(pos+1);
            }

            //win/linux 下载到tomcat工作目录下，每创建一个tomcat需要重新跟换磁盘文件位置

            //win截取字符，首先在tomcat的temp下新建file文件夹
            //url = StrUtil.sub(request.getServletContext().getRealPath("/"),0, 41) + "\\files\\" + IdUtil.randomUUID()+"="+filename;
            //System.out.println(url);

            //linux2
            //url = "/tmp/files/"+filename;

            //linux3
            url = "/tmp/files/"+IdUtil.randomUUID()+"="+filename;
        }

        try {
            //linux创建上传路径
            File files = new File(url);

            //判断映射文件的父文件是否存在
            if(!files.getParentFile().exists()){
                files.getParentFile().mkdirs(); //创建所有父文件夹
            }
            file.transferTo(files); //保存上传的文件

            //上传到数据库
            //1.若md5相同则源文件表数+1
            //2.将存有的源文件添加至用户文件表
            //3.删除上传过来的源文件并反馈给用户

            String md5 = GetFileMD5.getMD5Three(url);
            OriginFile queryByMD5 = originFileMapper.queryByMD5(md5);

            if(queryByMD5 != null){
                //源文件数+1
                queryByMD5.setFileCount(queryByMD5.getFileCount()+1);

                originFileMapper.updateOriginFile(queryByMD5);

                //将存有的源文件添加至用户文件表
                UserFile userFile = new UserFile(user.getUserId(),uploadId,queryByMD5.getOriginFileId(),filename,queryByMD5.getFileSize(),queryByMD5.getFileType(),1,now,now,now);
                userFileMapper.addFile(userFile);

                //删除上传的源文件并反馈
                FileUtil.del(url);
                return "{\"code\":0, \"msg\":\"success\", \"fileUrl\":\"" + "保存到当前浏览的文件夹下" + "\"}";

            } else {
                //获取文件大小
                Long fileSize = file.getSize();

                //获取文件类型
                String type = FileTypeUtil.getType(FileUtil.file(url));

                //System.out.println(file.getSize());

                //将上传的源文件写入数据库
                OriginFile originFile = new OriginFile(GetFileMD5.getMD5Three(url), fileSize, type, url, 1, 1, now, now);
                originFileMapper.addOriginFile(originFile);

                //将上传的文件的文件列表写入数据库
                UserFile userFile = new UserFile(user.getUserId(),uploadId,originFileMapper.queryByURL(url).getOriginFileId(),filename,fileSize,type,1,now,now,now);
                userFileMapper.addFile(userFile);
                return "{\"code\":0, \"msg\":\"success\", \"fileUrl\":\"" + "保存到当前浏览的文件夹下" + "\"}";

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "{\"code\":0, \"msg\":\"success\", \"fileUrl\":\"" + "保存到您当前浏览文件夹下" + "\"}";

    }


}
