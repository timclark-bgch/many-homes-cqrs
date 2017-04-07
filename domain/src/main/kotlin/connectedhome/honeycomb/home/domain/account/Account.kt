package connectedhome.honeycomb.home.domain.account

import connectedhome.honeycomb.home.domain.Aggregate

sealed class Event
data class Created(val owner: String) : Event()
data class Suspended(val reason: String) : Event()
data class Reactivated(val reason: String) : Event()
data class Closed(val reason: String) : Event()
data class HomeEntitlementAdded(val maxUsers: Int) : Event()

sealed class Status {
	class Active : Status()
	class Suspended : Status()
	class Closed : Status()
}

data class HomeEntitlement(val maxUsers: Int)

data class State(val status: Status = Status.Active(), val entitlements: List<HomeEntitlement> = listOf())

class Account(state: State, events: List<Event>) : Aggregate<State, Event>(state, events) {
	override fun apply(event: Event): State = when (event) {
		is Created -> state
		is Suspended -> state.copy(status = Status.Suspended())
		is Reactivated -> state.copy(status = Status.Active())
		is Closed -> state.copy(status = Status.Closed())
		is HomeEntitlementAdded -> state.copy(entitlements = updateEntitlements(state, event.maxUsers))
	}

	private fun updateEntitlements(state: State, maxUsers: Int): List<HomeEntitlement> {
		val entitlements = state.entitlements.toMutableList()
		entitlements.add(HomeEntitlement(maxUsers))

		return entitlements
	}

	fun suspend(reason: String): List<Event> =
		mutate {
			when (state.status) {
				is Status.Active -> listOf(Suspended(reason))
				else -> listOf()
			}
		}

	fun reactivate(reason: String): List<Event> =
		mutate {
			when (state.status) {
				is Status.Closed, is Status.Suspended -> listOf(Reactivated(reason))
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

	fun addHomeEntitlement(maxUsers: Int): List<Event> =
		mutate {
			when (state.status) {
				is Status.Active -> listOf(HomeEntitlementAdded(maxUsers))
				else -> emptyList()
			}
		}

}


