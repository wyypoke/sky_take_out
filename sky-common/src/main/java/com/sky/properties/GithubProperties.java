package com.sky.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * GitHub API 配置属性
 */
@Component
@ConfigurationProperties(prefix = "sky.github")
@Data
public class GithubProperties {

    /**
     * GitHub 仓库所有者 (Owner)，例如: wyypoke
     */
    private String owner;

    /**
     * GitHub 仓库名称 (Repository)，例如: sky_take_out
     */
    private String repo;

    /**
     * GitHub 个人访问令牌 (Personal Access Token, PAT)，用于API认证
     * 必须具有 repo 权限
     */
    private String token;

}