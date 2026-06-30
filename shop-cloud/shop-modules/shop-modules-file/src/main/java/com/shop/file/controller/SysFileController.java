package com.shop.file.controller;

import com.shop.core.domain.model.SysFile;
import com.shop.core.result.R;
import com.shop.file.service.SysFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
@RestController
@Slf4j
public class SysFileController {

    @Autowired
    private SysFileService sysFileService;

    /**
     * 文件上传请求
     */
    @PostMapping("upload")
    public R<SysFile> upload(MultipartFile file) {
        try {
            // 上传并返回访问地址
            Map<String, String> fileData = sysFileService.uploadFile(file);
            SysFile sysFile = new SysFile();
            sysFile.setFileName(fileData.get("fileName"));
            sysFile.setSysFileName(fileData.get("newFileName"));
            sysFile.setUrl(fileData.get("pathFileName"));
            sysFile.setUrlIp(fileData.get("pathIpFileName"));
            return R.ok(sysFile);
        } catch (Exception e) {
            log.error("上传文件失败", e);
            return R.failed(e.getMessage());
        }
    }
}
