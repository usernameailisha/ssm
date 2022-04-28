package com.wh.demo.utils;

import com.qiniu.common.Zone;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

/**
 * 通过七牛云的工具类，来上传图片到七牛云服务器
 */
public class QiniuUploadUtils {

    //设置好账号的ACCESS_KEY和SECRET_KEY;这两个登录七牛账号里面可以找到
    static String ACCESS_KEY = "ehbaYTqzl6MT2sizb1fzRp9D7Ohr20lJZQxzR2bd";
    static String SECRET_KEY = "FB2ToVupp-FbKCzBGUfmW6poTDv5RTupAslQhxyH";
    //要上传的空间;对应到七牛上（自己建文件夹 注意设置公开）
    static String bucketname = "phonew";
    //上传文件的路径 ;本地要上传文件路径
    //  String FilePath = "C:\\Users\\Administrator\\Pictures\\good\\g1002\\g100201.jpg";
    //七牛云服务器存储空间的域名
    static String yuName = "http://ra1xmugme.hn-bkt.clouddn.com/";
    //密钥配置
    static Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
    //创建上传对象  Zone.zone2()表示为华南地区   0-4  华东  华北  华南  北美  东南亚
    static UploadManager uploadManager = new UploadManager(new Configuration(Zone.zone2()));
    //简单上传，使用默认策略，只需要设置上传的空间名就可以了
    public static String getUpToken(){
        return auth.uploadToken(bucketname);
    }

    //实现图片的上传
    public static R uploadImage(MultipartFile file){
        try{
            //1.重新生成文件的名称-不重复的
            String newName = UUID.randomUUID().toString().replace("-", "");
            //200b4b6ced284daaaca89ba6ff0f85ae.png
            //2.获取原文件的后缀 .png
            String oldName = file.getOriginalFilename();
            String suffix = oldName.substring(oldName.lastIndexOf("."));
            //3.拼接成新的文件的名字
            String newFileName = newName + suffix;
            //4.实现文件上传
            uploadManager.put(file.getBytes(),newFileName,getUpToken());
            //5.返回域名+图片名，让前端能够显示
            return R.ok(yuName+newFileName);
        }catch (Exception e){
            return R.error("图片上传失败！");
        }

    }

    public static void main(String[] args) {
        //f50c7d73-bbd7-4341-a640-db4a95f85e79
        //2cb88ffb-e6b0-45c7-af4f-bfa1693df504
        //200b4b6ced284daaaca89ba6ff0f85ae
        System.out.println(UUID.randomUUID().toString().replace("-",""));
    }

}