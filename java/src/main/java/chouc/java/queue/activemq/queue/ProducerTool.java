package chouc.java.queue.activemq.queue;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class ProducerTool {        
    private String user = ActiveMQConnection.DEFAULT_USER;         
    private String password = ActiveMQConnection.DEFAULT_PASSWORD;
    private String url = "failover:(tcp://10.211.55.6:61616)";
    private String subject = "myqueue";      
    private Destination destination = null;      
    private Connection connection = null;      
    private Session session = null;      
    private MessageProducer producer = null;
    // 初始化   
    private void initialize() throws JMSException, Exception {      
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(      
                user, password, url);      
        connection = connectionFactory.createConnection();      
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);      
        destination = session.createQueue(subject);      
        producer = session.createProducer(destination);      
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);      
    }
 // 发送消息          
    public void produceMessage(String message) throws JMSException, Exception {      
        initialize();      
        TextMessage msg = session.createTextMessage(message);      
        connection.start();      
        System.out.println("Producer:->Sending message: " + message);      
        producer.send(msg);      
        System.out.println("Producer:->Message sent complete!");      
    }
    // 关闭连接      
    public void close() throws JMSException {      
        System.out.println("Producer:->Closing connection");      
        if (producer != null)      
            producer.close();      
        if (session != null)      
            session.close();      
        if (connection != null)      
            connection.close();      
    }      
}      