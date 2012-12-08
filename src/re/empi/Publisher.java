package re.empi;

public interface Publisher<T> {
	public void publish(T message);
	public void subscribe(Reciever<T> reciever);
}
