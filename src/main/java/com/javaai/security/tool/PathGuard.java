package com.javaai.security.tool;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;

/**
 * 工具参数校验：文件路径越界防护。
 *
 * <p><b>原则</b>：模型给出的一切参数都不可信 —— 工具名合法 ≠ 参数合法。
 * 攻击者可以诱导模型输出 {@code ../../etc/passwd} 这类路径。
 */
@Component
public class PathGuard {

    /** 白名单根目录：所有文件访问必须落在这个目录内 */
    private final Path baseDir;

    public PathGuard() {
        this(Path.of("/data/docs"));
    }

    public PathGuard(Path baseDir) {
        this.baseDir = baseDir;
    }

    /**
     * 把模型 / 用户给的相对路径解析到白名单目录内；越界直接抛异常。
     *
     * <p>关键两步：{@code normalize()} 消除 {@code ..}；{@code startsWith(base)} 二次校验。
     */
    public Path resolveSafe(String userProvidedPath) {
        Path base;
        try {
            base = baseDir.toRealPath();
        } catch (IOException e) {
            throw new SecurityException("白名单目录不可用: " + baseDir, e);
        }

        Path resolved = base.resolve(userProvidedPath).normalize();
        if (!resolved.startsWith(base)) {
            throw new SecurityException("路径越界，已拦截: " + userProvidedPath);
        }
        return resolved;
    }
}
