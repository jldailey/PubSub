package re.empi;

import java.util.LinkedList;

public class Channel<T> implements Publisher<T> {
	private LinkedList<Reciever<T>> subscribers = new LinkedList<Reciever<T>>();

	public void publish(T message) {
		for( Reciever<T> r : subscribers ) {
			r.recv(message);
		}
	}
	public void subscribe(Reciever<T> reciever) {
		subscribers.add(reciever);
	}
	public void unsubscribe(Reciever<T> reciever) {
		subscribers.remove(reciever);
	}

}

