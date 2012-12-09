

public class Main {
	public static void main(String[] args) {
		System.out.println("Hello World.");
		Reciever<String> log = new Reciever<String>() {
			public void recv(String message) {
				System.out.println("log: " + message);
			}
		};
		Channel<String> c = new Channel<String>();
		c.subscribe(log);
		c.publish("hello to channel c");
		Channel<String> a = new Channel<String>();
		Channel<String> b = new Channel<String>();
		a.pipeTo(b);
		b.subscribe(log);
		a.publish("hello to channel 'b' (via a)");

		HashRouter<String> h = new HashRouter<String>();
		h.subscribe("channel-one", log);
		h.publish("channel-one", "hashrouter to channel-one.");
		h.publish("channel-two", "hashrouter to channel-two.");
		h.publish("channel-one", "hashrouter to channel-one again.");

		PrefixRouter<String> p = new PrefixRouter<String>();
		p.subscribe("channel-one", log);
		p.subscribe("channel-two", log);
		p.publish("channel-one", "prefixrouter to channel-one");
		p.publish("channel-two", "prefixrouter to channel-two");
		p.publish("channel", "prefixrouter to both channels");
	}

}
