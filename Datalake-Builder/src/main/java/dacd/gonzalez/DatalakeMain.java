package dacd.gonzalez;

import javax.jms.JMSException;

public class DatalakeMain {
    public static void main(String[] args) throws JMSException {
        Subscriber subscriber = new AMQTopicSubscriber(args[0]);
        Listener listener = new FileStateEventBuilder(args[1]);
        subscriber.receive(listener);

    }

}
