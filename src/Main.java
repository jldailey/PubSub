

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

		PrefixRouter<String> p = new PrefixRouter<String>();
		p.subscribe("channel-one", log);
		p.subscribe("channel-two", log);
		p.publish("channel-one", "to channel-one");
		p.publish("channel-two", "to channel-two");
		p.publish("channel", "to both channels");
		PrefixRouter<String> q = new PrefixRouter<String>();
		q.pipeTo(p);
		q.publish("channel", "to both channels via router q");
	}

}
