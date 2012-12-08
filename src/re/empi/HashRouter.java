package re.empi;

import java.util.HashMap;

@SuppressWarnings("serial")
public class HashRouter<T>
	extends HashMap<String, Channel<T>>
	implements Router<T> {
	public void publish(String channel, T message) {
		check(channel);
		get(channel).publish(message);
	}
	public void subscribe(String channel, Reciever<T> reciever) {
		check(channel);
		get(channel).subscribe(reciever);
	}
	private void check(String channel) {
		if( ! containsKey(channel))
			put(channel, new Channel<T>());
	}
	public static void main(String[] args) {
		System.out.println("Hello World.");
		Reciever<String> log = new Reciever<String>() {
			public void recv(String message) {
				System.out.println("log: " + message);
			}
		};
		Channel<String> c = new Channel<String>();
		c.subscribe(log);
		c.publish("Hello  World.");

		HashRouter<String> h = new HashRouter<String>();
		h.subscribe("channel-one", log);
		h.publish("channel-one", "Hello World.");
		h.publish("channel-two", "Hello World.");
		h.publish("channel-one", "Hello World.");
		
		PrefixRouter<String> p = new PrefixRouter<String>();
		p.subscribe("channel-one", log);
		p.subscribe("channel-two", log);
		p.publish("channel-one", "to channel-one");
		p.publish("channel-two", "to channel-two");
		p.publish("channel", "to both channels");
	}

}
