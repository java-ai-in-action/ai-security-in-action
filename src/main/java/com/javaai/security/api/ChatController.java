package com.javaai.security.api;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 演示入口：POST /api/chat
 *
 * <p>注意异常处理：**绝不要把 SecurityException 的细节返回给用户**——
 * 否则"拦截提示"本身就成了攻击者的探测工具（能判断哪条规则命中）。
 */
@RestController
@RequestMapping("/api")
public class ChatController {

    private final ChatClient secureChatClient;

    public ChatController(ChatClient secureChatClient) {
        this.secureChatClient = secureChatClient;
    }

    @PostMapping("/chat")
    public Map<String, Object> chat(@RequestBody ChatRequest req) {
        try {
            String answer = secureChatClient.prompt()
                    .user(req.message())
                    .call()
                    .content();
            return Map.of("answer", answer);
        } catch (SecurityException e) {
            // 兜底话术：统一口径，不暴露命中原因
            return Map.of(
                    "answer", "抱歉，这个问题我无法回答。你可以问我订单或物流相关的问题。",
                    "blocked", true);
        }
    }

    public record ChatRequest(String userId, String message) {
    }
}
