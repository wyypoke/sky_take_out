package com.sky.service.impl;

import com.sky.properties.GithubProperties;
import com.sky.result.Result;
import com.sky.service.CommonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class CommonServiceImpl implements CommonService {
    @Autowired
    private GithubProperties githubProperties;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 将 MultipartFile 上传到 GitHub 仓库
     * @param file 待上传的文件
     * @return 包含文件URL或错误信息的Result
     */
    public Result<String> uploadFile(MultipartFile file) {
        final String GITHUB_OWNER = githubProperties.getOwner(); // 仓库所有者
        final String GITHUB_REPO = githubProperties.getRepo(); // 仓库名称
        final String GITHUB_TOKEN = githubProperties.getToken();
        if (file.isEmpty()) {
            return Result.error("上传文件不能为空。");
        }
        // 1. 获取文件内容并进行 Base64 编码
        byte[] fileBytes = null;
        try {
            fileBytes = file.getBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String base64Content = Base64.getEncoder().encodeToString(fileBytes);

        // 2. 构造 API URL 和文件路径 (使用原始文件名作为仓库中的路径)
        // --- START: 文件名修改逻辑 ---
        String originalFilename = file.getOriginalFilename();

        // 1. 提取文件扩展名（例如：.jpg, .png）
        String fileExtension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        // 2. 生成基于 UUID 的新文件名
        // 这是我们将用于 GitHub 仓库的路径 (filePath)
        String fileName = UUID.randomUUID().toString() + fileExtension;
        // --- END: 文件名修改逻辑 ---
        // 注意：文件路径需要进行 URL 编码，这里简化处理。
        String apiUrl = String.format("https://api.github.com/repos/%s/%s/contents/%s",
                GITHUB_OWNER, GITHUB_REPO, fileName);

        // 3. 构造请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        // 认证：使用 Personal Access Token
        headers.setBearerAuth(GITHUB_TOKEN);
        headers.set("Accept", "application/vnd.github.v3+json");

        // 4. 构造请求 Body (JSON)
        Map<String, String> body = new HashMap<>();
        // 提交信息
        body.put("message", "feat: Upload file " + fileName + " via API");
        // Base64 编码后的文件内容
        body.put("content", base64Content);
        // 注意：如果是更新现有文件，还需要 'sha' 字段。这里假设是新增文件。

        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(body, headers);

        // 5. 执行 PUT 请求
        ResponseEntity<Map> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.PUT,
                requestEntity,
                Map.class
        );

        // 6. 处理响应
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            // 从响应体中获取内容对象
            Map<String, Object> content = (Map<String, Object>) response.getBody().get("content");

            // 获取仓库内的文件路径
            String filePath = (String) content.get("path");

            // **重点修改：构造 Raw URL**
            // 格式: https://raw.githubusercontent.com/{owner}/{repo}/{branch}/{path}
            String rawFileUrl = String.format(
                    "https://raw.githubusercontent.com/%s/%s/%s/%s",
                    githubProperties.getOwner(),
                    githubProperties.getRepo(),
                    "main",
                    filePath
            );

            // 如果文件名包含中文或特殊字符，需要进行 URL 编码
            // 这里我们先假设 filePath 已经被正确编码（通常 Spring 的 MultipartFile 会处理好文件名）

            return Result.success(rawFileUrl);
        } else {
            log.info("GitHub API 调用失败");
            return Result.error("GitHub API 调用失败，状态码：" + response.getStatusCodeValue());
        }

    }
}
