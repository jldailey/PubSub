

import java.util.HashMap;

@SuppressWarnings("serial")
public class BaseRouter<K, T>
	extends HashMap<K, Channel<T>>
	implements Router<K, T> {

	public void publish(K channel, T message) {
		if( monitor.size() > 0 )
			monitor.publish(new Message(channel, message));
		check(channel).publish(message);
	}

	public void subscribe(K channel, Reciever<T> reciever) {
		check(channel).subscribe(reciever);
	}

	// these message objects are used by pipeTo
	class Message {
		public K channel;
		public T message;
		public Message(K c, T m) { channel = c; message = m; }
	}
	class Pipe implements Reciever<Message> {
		Router<K, T> dest;
		public Pipe(Router<K,T> d) { dest = d; }
		public void recv(Message message) {
			dest.publish(message.channel, message.message);
		}
	}
	Channel<Message> monitor = new Channel<Message>();

	public void pipeTo(Router<K, T> router) {
		monitor.subscribe(new Pipe(router));
	}
	public void pipeTo(K channel, Channel<T> chan) {
		check(channel).pipeTo(chan);
	}
	private Channel<T> check(K channel) {
		if( ! containsKey(channel))
			return put(channel, new Channel<T>());
		return get(channel);
	}

}