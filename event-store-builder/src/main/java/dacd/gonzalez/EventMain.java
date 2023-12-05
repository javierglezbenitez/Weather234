package dacd.gonzalez;

import javax.jms.JMSException;

public class EventMain {
    public static void main(String[] args) throws JMSException {
        Subscriber suscriber = new AMQTopicSubscriber(args[0]);
        Listener listener = new FileStateEventBuilder(args[1]);
        suscriber.start(listener,args[2]);
    }
}