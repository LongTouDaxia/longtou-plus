package com.mall.LongTou.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
@Slf4j
@Component
public class SeckillOrderResultWebSocketHandler extends TextWebSocketHandler {
    // 线程安全的 Map：tokenId -> WebSocketSession
    private static final ConcurrentHashMap<String, WebSocketSession> sessionMap = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        //从路径中取出参数
        String path = session.getUri().getPath().replace("/order", "order");

        String tokenId = path.substring(path.lastIndexOf("/") + 1);
        sessionMap.put(tokenId,session);

    }



    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // 连接关闭时，从 map 中移除该 session（需要通过 session 反查 tokenId，这里简化：可遍历删除）
        sessionMap.entrySet().removeIf(entry -> entry.getValue().equals(session));
    }

    // 供其他组件调用：推送订单结果
    public static void pushResult(String tokenId, String result) {
        WebSocketSession session = sessionMap.get(tokenId);
        if (session != null && session.isOpen()) {
            try {
                session.sendMessage(new TextMessage(result));
                log.info("像前端推送订单id");
                log.info("tokenId值为：{}",sessionMap.containsKey(tokenId));
            } catch (IOException e) {
                // 日志记录
            }
        }
    }
}
