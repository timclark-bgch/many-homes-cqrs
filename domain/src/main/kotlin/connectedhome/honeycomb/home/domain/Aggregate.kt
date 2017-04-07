package connectedhome.honeycomb.home.domain

abstract class Aggregate<S, E>(protected var state: S, events: List<E>) {
	init {
		events.forEach { e -> state = apply(e) }
	}

	abstract protected fun apply(event: E): S

	protected fun mutate(mutation: () -> List<E>): List<E> {
		val events = mutation()

		events.forEach { e -> state = apply(e) }

		return events
	}
}


