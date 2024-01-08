package dacd.gonzalez;

import dacd.gonzalez.Controller.BrokerReceiver;
import dacd.gonzalez.Controller.Datamart;
import dacd.gonzalez.Controller.Receiver;
import dacd.gonzalez.Controller.Storer;

import javax.jms.JMSException;

public class VacationMain {
    public static void main(String[] args) throws JMSException {

        Receiver receiver = new BrokerReceiver(args[0]);
        Storer storer = new Datamart(args[1]);
        receiver.receiveMessage(storer);




    }
}
