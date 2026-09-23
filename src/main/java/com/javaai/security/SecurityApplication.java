package com.javaai.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * AI 应用安全四层防御示例入口。
 *
 * <p>配套文章：篇9《上线 AI 客服 30 天，我们被薅了 12 万羊毛：一个 Prompt Injection 真实复盘》
 */
@SpringBootApplication
public class SecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecurityApplication.class, args);
    }
}
