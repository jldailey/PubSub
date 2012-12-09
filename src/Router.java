

public interface Router<K, T> {
	public void publish(K route, T payload);
	public void subscribe(K route, Reciever<T> reciever);
}
