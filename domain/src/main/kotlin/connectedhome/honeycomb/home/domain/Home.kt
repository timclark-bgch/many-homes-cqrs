package connectedhome.honeycomb.home.domain

data class Owner(val id: String)

sealed class Event
data class Created(val owner: Owner) : Event()
data class Migrated(val owner: Owner) : Event()
data class Suspended(val reason: String) : Event()
data class Closed(val reason: String) : Event()
data class Reactivated(val reason: String) : Event()

class HomeAggregate(private var state: HomeState, events: List<Event>) {
	init {
		events.forEach { e -> state = apply(e) }
	}

	private fun apply(event: Event): HomeState {
		return when (event) {
			is Suspended -> state.copy(status = Status.Suspended())
			is Reactivated -> state.copy(status = Status.Active())
			is Closed -> state.copy(status = Status.Closed())
			else -> state
		}
	}

	private fun mutate(mutation: () -> List<Event>): List<Event> {
		val events = mutation()

		events.forEach { e -> state = apply(e) }

		return events
	}

	fun suspend(reason: String): List<Event> =
		mutate {
			when (state.status) {
				is Status.Active -> listOf(Suspended(reason))
				else -> listOf()
			}
		}

	fun close(reason: String): List<Event> =
		mutate {
			when (state.status) {
				is Status.Active, is Status.Suspended -> listOf(Closed(reason))
				else -> listOf()
			}
		}

	fun reactivate(reason: String): List<Event> =
		mutate {
			when (state.status) {
				is Status.Suspended, is Status.Closed -> listOf(Reactivated(reason))
				else -> listOf()
			}
		}
}

sealed class Status {
	class Active : Status()
	class Suspended : Status()
	class Closed : Status()
}

data class HomeState(val status: Status = Status.Active())

