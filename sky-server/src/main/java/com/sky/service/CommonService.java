package com.sky.service;

import com.sky.result.Result;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CommonService {


    public Result<String> uploadFile(MultipartFile file);
}
