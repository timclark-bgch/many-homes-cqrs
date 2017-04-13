package honeycomb.home.events;

public final class EventHolder {
	public final String descriptor;
	public final byte[] data;

	public EventHolder(final String descriptor, final byte[] data) {
		this.descriptor = descriptor;
		this.data = data;
	}
}
