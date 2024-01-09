package dacd.gonzalez;

import dacd.gonzalez.Controller.BrokerReceiver;
import dacd.gonzalez.Controller.Datamart;
import dacd.gonzalez.Controller.Receiver;
import dacd.gonzalez.Controller.Storer;
import dacd.gonzalez.Model.CommandBuilder;
import dacd.gonzalez.View.UserInterface;

import javax.jms.JMSException;
import java.util.concurrent.TimeUnit;

public class VacationMain {
    public static void main(String[] args) throws JMSException {
        Receiver receiver = new BrokerReceiver(args[0]);
        Storer storer = new Datamart(args[1]);
        receiver.receiveMessage(storer);

        Thread secondPartThread = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(8);

                CommandBuilder commandBuilder = new CommandBuilder();
                UserInterface userInterface = new UserInterface(commandBuilder);
                userInterface.execute();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        secondPartThread.start();

        try {
            secondPartThread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
