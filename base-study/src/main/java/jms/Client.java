package jms;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;

import javax.jms.*;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws Exception{
        Connection connection = new ActiveMQConnectionFactory("tcp://localhost:61616").createConnection();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination destinationService = new ActiveMQQueue("service");
        MessageProducer producer = session.createProducer(destinationService);

        System.out.println("started");
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String message = scanner.nextLine();
            TextMessage textMessage = session.createTextMessage(message);
            producer.send(textMessage);
            if ("exit".equals(message)) {
                break;
            }
        }
        session.close();
        connection.close();
    }
}
