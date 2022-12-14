package com.yl.controller;

import com.yl.mapper.OriginFileMapper;
import com.yl.pojo.OriginFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

@Controller
public class DownloadController {
    @Autowired
    OriginFileMapper originFileMapper;
    @RequestMapping("/user/download/{originId}")
    public String downloads(HttpServletResponse response, HttpServletRequest request, @PathVariable("originId") Integer originId) throws IOException {
        //下载地址
        OriginFile originFile = originFileMapper.queryById(originId);
        String path = originFile.getFileUrl();

        int ind = path.lastIndexOf("=");
        if(ind == -1){
            ind = path.lastIndexOf("/");
        }

        String fileName = path.substring(ind+1,path.length());

        //1.设置response 响应头
        response.reset();   //设置页面不缓存，清空buffer
        response.setCharacterEncoding("UTF-8");
        response.setContentType("multipart/form-data"); //二进制传输数据

        //设置响应头
        response.setHeader("Content-Disposition",
                "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));

        File file = new File(path);

        //2.读取文件 输入流
        InputStream input = new FileInputStream(file);

        //3.写出文件 输出流
        OutputStream out = response.getOutputStream();

        byte[] buff = new byte[8192];
        int index = 0;

        //4.执行写出操作
        while((index = input.read(buff)) != -1){
            out.write(buff, 0, index);
            out.flush();
        }
        out.close();
        input.close();
        return null;

    }
}
