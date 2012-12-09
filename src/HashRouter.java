

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

}
