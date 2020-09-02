package com.learn.web_chat;

import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@ServerEndpoint("/chat")
public class WebSocket {

    /**
     * 在线人数
     */
    private static int onlineNumber = 0;

    /**
     * 保存WebSocket
     */
    private static Set<WebSocket> clients = new CopyOnWriteArraySet<WebSocket>();
    /**
     * 会话
     */
    private Session session;

    private String username;
    /**
     * 建立连接
     *
     * @param session
     */
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        clients.add(this); // 加入set集合中
        addOnlineNumber(); // 在线人数增加
        try {
            sendMessage("您已经连接成功，当前在线人数为："+getOnlineNumber());//发送连接成功消息
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("有新的连接加入！当前在线人数为" + getOnlineNumber());
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {

        clients.remove(this); // 从clients中删除
        subOnlineNumber(); // 在线人数减1
        for (WebSocket item : clients) {
                try {
                    item.sendMessage("当前在线人数为："+getOnlineNumber());
                } catch (IOException e) {
                    e.printStackTrace();
                    continue;
                }
        }
        System.out.println("有连接关闭！当前在线人数为" + getOnlineNumber());
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("来自客户端的消息:" + message);

        // 群发消息
        for (WebSocket item : clients) {
            try {
                item.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
        }
    }

    /**
     * 发生错误
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("发生错误");
        error.printStackTrace();
    }

    /**
     * 实现服务器主动推送
     * @param message
     * @throws IOException
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }


    public static synchronized int getOnlineNumber() {
        return onlineNumber;
    }
    public static synchronized void addOnlineNumber() {
        WebSocket.onlineNumber++;
    }
    public static synchronized void subOnlineNumber() {
        WebSocket.onlineNumber--;
    }

}
