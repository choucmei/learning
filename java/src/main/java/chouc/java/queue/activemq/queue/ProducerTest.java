package chouc.java.queue.activemq.queue;
import javax.jms.JMSException;

public class ProducerTest {      
     
    /**    
     * @param args    
     * @throws Exception 
     * @throws JMSException 
     */     
    public static void main(String[] args){

        ProducerTool producer = new ProducerTool();
        try {
            System.out.println("2");
            producer.produceMessage("Hello, world!");
            System.out.println("3");
            producer.close();
            System.out.println("4");
        } catch (Exception e) {

        }
        System.out.println("---");

    }      
}      

