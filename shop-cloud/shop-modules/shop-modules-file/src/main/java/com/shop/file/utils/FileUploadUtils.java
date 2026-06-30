package com.shop.file.utils;

import com.shop.core.constant.Constants;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FileUploadUtils {
    public static Map<String,String> upload(String filePath, MultipartFile file) {
        if (file.isEmpty()){
            throw new RuntimeException("文件为空！");
        }
        //文件名字（传进来的文件名）
        String fileName = file.getOriginalFilename();
        //生成文件名(新文件名)
        String newFileName = UUID.randomUUID().toString().replace("-","") + fileName.substring(fileName.lastIndexOf("."));
        //创建年月日文件夹
        Calendar date = Calendar.getInstance();
        //完整的url
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(filePath);
        stringBuilder.append("/");
        stringBuilder.append(date.get(Calendar.YEAR));
        stringBuilder.append("/");
        stringBuilder.append(date.get(Calendar.MONTH) + 1);
        stringBuilder.append("/");
        stringBuilder.append(date.get(Calendar.DATE));
        stringBuilder.append("/");
        stringBuilder.append(newFileName);
        String fileUrl = stringBuilder.toString(); //文件的最终路径
        File parent = new File(fileUrl);
        if (!parent.getParentFile().exists()){
            parent.getParentFile().mkdirs();
        }
        Map<String,String> map = new HashMap<>();

        try{
            //开始上传文件
            file.transferTo(parent);
        }catch (IOException e){
            e.printStackTrace();
            map.put("msg","上传失败");
            return map;
        }

        String pathFileName = getPathFileName(fileUrl, filePath);//拼接之后返给前台的路径
        map.put("fileName",fileName);      //原本的文件名字
        map.put("newFileName",newFileName);//防止文件名重复的新文件名
        map.put("pathFileName",pathFileName);//最终展示的路径
        return map;
    }

    public final static String getPathFileName(String uploadDir,String filePath) {
        int dirLastIndex = filePath.length() + 1;
        String currentDir = substring(uploadDir, dirLastIndex);
        String pathFileName = Constants.RESOURCE_PREFIX + "/" + currentDir ;
        return pathFileName;
    }

    /**
     * 截取字符串
     *
     * @param str 字符串
     * @param start 开始
     * @return 结果
     */
    public static String substring(final String str, int start) {
        if (str == null) {
            return "";
        }
        if (start < 0) {
            start = str.length() + start;
        }
        if (start < 0) {
            start = 0;
        }
        if (start > str.length()) {
            return "";
        }
        return str.substring(start);
    }
}
