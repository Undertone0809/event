package com.event.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * WebSocketServer
 * @author 孤雏
 */
@Component
@ServerEndpoint("/websocket/{recipientId}")
@Slf4j
public class WebSocketServer {
   // private static AmqpTemplate amqpTemplate;

    private static RabbitTemplate rabbitTemplate;
    @Autowired
    public void setService(RabbitTemplate rabbitTemplate){
        WebSocketServer.rabbitTemplate = rabbitTemplate;
    }

    private static int onlineCount = 0;
    private static CopyOnWriteArraySet<WebSocketServer> webSocketSet = new CopyOnWriteArraySet<>();
    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    //接收sid
    private Integer recipientId;
    @OnOpen
    public void onOpen(Session session,@PathParam("recipientId") Integer recipientId) throws IOException {
        this.session = session;
        webSocketSet.add(this);     //加入set中
        addOnlineCount();           //在线数加1
//        log.info("有新窗口开始监听:"+recipientId+",当前在线人数为" + getOnlineCount());
        this.recipientId=recipientId;
        List<Object> unread = new CopyOnWriteArrayList<>();
        Object o = null;
        final ConnectionFactory factory = rabbitTemplate.getConnectionFactory();
        final Connection connection = factory.createConnection();
        try {
            // 下面3行是我加的
            Channel channel = connection.createChannel(false);
            channel.queueDeclare(String.valueOf(recipientId), true, false, false, null);
            QueueBuilder.durable(String.valueOf(recipientId));
            AMQP.Queue.DeclareOk declareOk = channel.queueDeclarePassive(String.valueOf(recipientId));
            int cnt = declareOk.getMessageCount();

            // System.err.println("推送未读消息，消息数量为："+cnt);
            while(cnt-->0) {
                o = rabbitTemplate.receiveAndConvert(String.valueOf(recipientId));
                unread.add(o);
            }
            connection.createChannel(false).queuePurge(String.valueOf(recipientId));
            connection.close();
        } catch (AmqpException e) {
            connection.createChannel(false).queueDeclare(String.valueOf(recipientId), true, false, false, null);
            connection.close();
        }
        sendMessage(JSON.toJSONString(unread));

        /*try {
            sendMessage(JSON.toJSONString(RestResponse.success()));
        } catch (IOException e) {
            log.error("websocket IO异常");
        }*/
    }
    @OnClose
    public void onClose() throws IOException {

        webSocketSet.remove(this);  //从set中删除
        subOnlineCount();           //在线数减1
        log.info("有一连接关闭！当前在线人数为" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息*/
    @OnMessage
    public void onMessage(String message, Session session) {
       log.info("收到来自窗口"+recipientId+"的信息:"+message);
    }
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误");
        error.printStackTrace();
    }

    /**
     * 实现服务器主动推送
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    public static void sendInfo(Integer senderId,String message,Integer recipientId) throws Exception {
        AtomicBoolean flag = new AtomicBoolean(true);
        webSocketSet.forEach(item -> {
            try {// 这里可以设定只推送给这个recipientId的，为null则全部推送
                if (recipientId == null) {
                    flag.set(false);
                    item.sendMessage(message);
                    log.info(senderId+"发送消息到" + item.recipientId + "，推送内容:" + message);
                } else if (item.recipientId.equals(recipientId)) {
                    item.sendMessage(message);
                    flag.set(false);
                }
            } catch (IOException e) {
                log.error("发生错误");
            }
        });
        if(flag.get()) throw new Exception("未在线");
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }
    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }
    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }
}
