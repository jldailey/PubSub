

public class Main {
	public static void main(String[] args) {
		System.out.println("Hello World.");
		Hub.Listening<String> log = new Hub.Listening<String>() {
			public void onMessage(String message) {
				System.out.println("log: " + message);
			}
		};
		Hub.Publisher<String> c = new Hub.Publisher<String>();
		c.subscribe(log);
		c.publish("hello to channel c");
		Hub.Publisher<String> a = new Hub.Publisher<String>();
		Hub.Publisher<String> b = new Hub.Publisher<String>();
		a.pipeTo(b);
		b.subscribe(log);
		a.publish("hello to channel 'b' (via a)");

		Hub.PrefixRouter<String> p = new Hub.PrefixRouter<String>();
		p.subscribe("channel-one", log);
		p.subscribe("channel-two", log);
		p.publish("channel-one", "to channel-one");
		p.publish("channel-two", "to channel-two");
		p.publish("channel", "to both channels");
		Hub.PrefixRouter<String> q = new Hub.PrefixRouter<String>();
		q.pipeTo(p);
		q.publish("channel", "to both channels via router q");
	}
}
