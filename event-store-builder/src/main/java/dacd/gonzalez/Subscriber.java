package dacd.gonzalez;

public interface Subscriber {
    void receive(Listener listener,String topicName);

}
