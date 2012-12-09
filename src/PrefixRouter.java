

import java.util.Iterator;

@SuppressWarnings("serial")
public class PrefixRouter<T>
	extends BaseRouter<String, T>
	{

	// store a tree for doing quick prefix searches
	// TODO: currently tragically wasteful of memory
	private Util.Trie routes = new Util.Trie();

	public void publish(String route, T message) {
		synchronized(routes) {
			routes.add(route);
			Iterator<String> channels = routes.find(route);
			while( channels.hasNext() ) {
				super.publish(channels.next(), message);
			}
		}
	}

	public void subscribe(String route, Reciever<T> reciever) {
		synchronized(routes) {
			routes.add(route);
			Iterator<String> channels = routes.find(route);
			while( channels.hasNext() ) {
				super.subscribe(channels.next(), reciever);
			}
		}
	}

}
