package com.shop.file.service.impl;

import com.shop.core.utils.ServerUrl;
import com.shop.file.common.PicCommon;
import com.shop.file.service.SysFileService;
import com.shop.file.utils.FileUploadUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Slf4j
@Service
public class SysFileServiceImpl  implements SysFileService {

    @Autowired
    private PicCommon common;

    @Autowired
    private ServerUrl serverUrl;

    @Override
    public Map<String, String> uploadFile(MultipartFile file) {
        PicCommon.ElPath path = common.getPath();
        // 上传并返回新文件名称
        Map<String, String> upload = FileUploadUtils.upload(path.getPath(), file);
        String urlIp = serverUrl.getUrl() + upload.get("pathFileName");
        String url = (String) upload.get("pathFileName");
        upload.put("fileName",upload.get("fileName"));
        upload.put("newFileName",upload.get("newFileName"));
        upload.put("pathFileName",url);
        upload.put("pathIpFileName",urlIp);
        return upload;
    }
}
