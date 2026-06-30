package com.shop.file.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface SysFileService {
    Map<String, String> uploadFile(MultipartFile file);
}
