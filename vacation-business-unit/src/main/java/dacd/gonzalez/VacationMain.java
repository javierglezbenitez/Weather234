package dacd.gonzalez;

import javax.jms.JMSException;

public class VacationMain {
    public static void main(String[] args) throws JMSException {

        Receiver receiver = new BrokerReceiver(args[0]);
        Storer storer = new Datamart(args[1]);
        receiver.receiveMessage(storer);


    }
}
