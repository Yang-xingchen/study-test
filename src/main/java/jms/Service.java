package jms;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;

import javax.jms.*;

public class Service {

    public static void main(String[] args) throws Exception {
        Connection connection = new ActiveMQConnectionFactory("tcp://localhost:61616").createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination destination = new ActiveMQQueue("service");
        MessageConsumer messageConsumer = session.createConsumer(destination);
        System.out.println("started");
        while(true) {
            TextMessage message = (TextMessage) messageConsumer.receive();
            System.out.println(message.getText());
            if ("exit".equals(message.getText())) {
                break;
            }
        }
        session.close();
        connection.close();
    }

}
