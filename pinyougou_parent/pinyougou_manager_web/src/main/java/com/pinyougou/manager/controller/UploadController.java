package com.pinyougou.manager.controller;

import entity.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import util.FastDFSClient;

@RestController
@RequestMapping("/upload")
public class UploadController {

    @Value("${FILE_SERVER_URL}")
    private String FILE_SERVER_URL;

    @RequestMapping("/uploadFile")
    public Result uploadFile(MultipartFile file){
        try {
            FastDFSClient fastDFSClient = new FastDFSClient("classpath:config/fdfs_client.conf");
            String extName = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);
            String path = fastDFSClient.uploadFile(file.getBytes(), extName);
            String url = FILE_SERVER_URL + path;
            String message = "{\"url\":\""+url+"\",\"message\":\"上传成功\"}";
            return new Result(true,message);
        } catch (Exception e) {
            return new Result(false,"上传失败");
        }
    }
}
