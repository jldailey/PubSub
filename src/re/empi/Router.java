package re.empi;

public interface Router<T> {
	public void publish(String route, T payload);
	public void subscribe(String route, Reciever<T> reciever);
}
