

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
	public int size() {
		return subscribers.size();
	}
	private class Pipe implements Reciever<T> {
		Channel<T> dest;
		public Pipe(Channel<T> d) {
			dest = d;
		}
		public void recv(T message) {
			dest.publish(message);
		}
	}
	public void pipeTo(Channel<T> d) {
		subscribe(new Pipe(d));
	}

}

