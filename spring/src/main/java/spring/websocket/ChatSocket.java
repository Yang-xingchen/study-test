package spring.websocket;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

@Component
//@ServerEndpoint("/chat")
public class ChatSocket extends AbstractWebSocketHandler {

    private static final AtomicLong count = new AtomicLong(0);

    private static final List<ChatSocket> chats = new CopyOnWriteArrayList<>();

//    private Session session;

//    @OnOpen
//    public void open(Session session){
//        chats.add(this);
//        this.session = session;
//        count.addAndGet(1);
//        chats.forEach(chatSocket -> chatSocket.send("有新的会话加入，当前在线人数"+count.get()+"人\n"));
//    }
//
//    @OnClose
//    public void close(){
//        chats.remove(this);
//        count.addAndGet(-1);
//        chats.forEach(chatSocket -> chatSocket.send("有会话退出，当前在线人数"+count.get()+"人\n"));
//    }
//
//    @OnMessage
//    public void message(String message, Session session){
//    }
//
//    private void send(String message){
//        try {
//            session.getBasicRemote().sendText(message);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    private WebSocketSession session;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        chats.add(this);
        this.session = session;
        count.addAndGet(1);
        chats.forEach(chatSocket -> chatSocket.send(new TextMessage("有新的会话加入，当前在线人数"+count.get()+"人\n")));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        chats.remove(this);
        count.addAndGet(-1);
        chats.forEach(chatSocket -> chatSocket.send(new TextMessage("有会话退出，当前在线人数"+count.get()+"人\n")));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        chats.forEach(chatSocket -> chatSocket.send(message));
    }

    private void send(TextMessage message){
        try {
            session.sendMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
