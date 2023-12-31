package dacd.gonzalez;

import javax.jms.JMSException;

public class EventMain {
    private static String topicName = "prediction.Hotel";
    public static void main(String[] args) throws JMSException {
        Subscriber subscriber = new AMQTopicSubscriber(args[0]);
        Listener listener = new FileStateEventBuilder();
        subscriber.receive(listener,topicName);
    }
}