
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Iterator;

/* A Hub is where pub/sub happens. */
public class Hub {
	/* This is what it means to be a singular publisher. */
	public static interface Publishing<T> {
		public void publish(T message);
		public void subscribe(Listening<T> reciever);
	}
	/* This is a plural publisher with multiple channels. */
	public static interface Routing<K, T> {
		public void publish(K route, T payload);
		public void subscribe(K route, Listening<T> reciever);
	}
	/* All publishers talk to an audience that can listen. */
	public static interface Listening<T> {
		public void onMessage(T message);
	}

	public static class Publisher<T> 
	extends LinkedList<Listening<T>>
	implements Publishing<T> {

		public void publish(T message) {
			for( Listening<T> r : this ) {
				r.onMessage(message);
			}
		}
		public void subscribe(Listening<T> reciever) {
			add(reciever);
		}
		public void unsubscribe(Listening<T> reciever) {
			remove(reciever);
		}
		private class Pipe implements Listening<T> {
			Publisher<T> dest;
			public Pipe(Publisher<T> d) {
				dest = d;
			}
			public void onMessage(T message) {
				dest.publish(message);
			}
		}
		public void pipeTo(Publisher<T> d) {
			subscribe(new Pipe(d));
		}

	}

	public static class Router<K, T>
	extends HashMap<K, Publisher<T>>
	implements Routing<K, T> {

		public void publish(K channel, T message) {
			if( monitor.size() > 0 )
				monitor.publish(new Message(channel, message));
			check(channel).publish(message);
		}

		public void subscribe(K channel, Listening<T> reciever) {
			if( reciever == null ) return;
			Publisher<T> c = check(channel);
			c.subscribe(reciever);
		}

		// these message objects are used by pipeTo
		class Message {
			public K channel;
			public T message;
			public Message(K c, T m) { channel = c; message = m; }
		}
		class Pipe implements Listening<Message> {
			Routing<K, T> dest;
			public Pipe(Routing<K,T> d) { dest = d; }
			public void onMessage(Message message) {
				dest.publish(message.channel, message.message);
			}
		}
		Publisher<Message> monitor = new Publisher<Message>();

		public void pipeTo(Routing<K, T> router) {
			monitor.subscribe(new Pipe(router));
		}
		public void pipeTo(K channel, Publisher<T> chan) {
			check(channel).pipeTo(chan);
		}
		private Publisher<T> check(K channel) {
			if( ! containsKey(channel))
				put(channel, new Publisher<T>());
			return get(channel);
		}

	}


	/* PrefixRouter is a router that dispatches to multiple channels
	 * that match a prefix:
	 * p.subscribe("abc", ...);
	 * p.subscribe("ab", ...);
	 * p.publish("a", ...); // like publishing on "abc" and "ab" channels.
	 */
	public static class PrefixRouter<T>
	extends Router<String, T>
	{

		// store a tree for doing quick prefix searches
		// TODO: currently tragically wasteful of memory
		private Util.Trie routes = new Util.Trie();

		public void publish(String route, T message) {
			routes.add(route);
			Iterator<String> channels = routes.find(route);
			while( channels.hasNext() ) {
				super.publish(channels.next(), message);
			}
		}

		public void subscribe(String route, Listening<T> reciever) {
			routes.add(route);
			Iterator<String> channels = routes.find(route);
			while( channels.hasNext() ) {
				super.subscribe(channels.next(), reciever);
			}
		}

	}

}