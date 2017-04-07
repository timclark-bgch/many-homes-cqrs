package connectedhome.honeycomb.home.domain.account

import connectedhome.honeycomb.home.domain.Aggregate

sealed class Event
data class Created(val owner: String) : Event()
data class Suspended(val reason: String) : Event()
data class Reactivated(val reason: String) : Event()
data class Closed(val reason: String) : Event()
data class HomeEntitlementAdded(val maxUsers: Int) : Event()
data class HomeAdded(val home: String) : Event()

sealed class Status {
	class Active : Status()
	class Suspended : Status()
	class Closed : Status()
}

data class HomeEntitlement(val maxUsers: Int)

data class State(
	val status: Status = Status.Active(),
	val entitlements: List<HomeEntitlement> = emptyList(),
	val homes: List<String> = emptyList())

private fun State.updated(maxUsers: Int): List<HomeEntitlement> {
	val entitlements = this.entitlements.toMutableList()

	entitlements.add(HomeEntitlement(maxUsers))

	return entitlements
}

private fun State.addHome(home: String): List<String> {
	val homes = this.homes.toMutableList()
	homes.add(home)

	return homes
}

private fun State.entitled(): Boolean =
	this.entitlements.size > this.homes.size

class Account(state: State, events: List<Event>) : Aggregate<State, Event>(state, events) {
	override fun apply(event: Event): State = when (event) {
		is Created -> state
		is Suspended -> state.copy(status = Status.Suspended())
		is Reactivated -> state.copy(status = Status.Active())
		is Closed -> state.copy(status = Status.Closed())
		is HomeEntitlementAdded -> state.copy(entitlements = state.updated(event.maxUsers))
		is HomeAdded -> state.copy(homes = state.addHome(event.home))
	}

	fun suspend(reason: String): List<Event> =
		mutate {
			when (state.status) {
				is Status.Active -> listOf(Suspended(reason))
				else -> emptyList()
			}
		}

	fun reactivate(reason: String): List<Event> =
		mutate {
			when (state.status) {
				is Status.Closed, is Status.Suspended -> listOf(Reactivated(reason))
				else -> emptyList()
			}
		}

	fun close(reason: String): List<Event> =
		mutate {
			when (state.status) {
				is Status.Active, is Status.Suspended -> listOf(Closed(reason))
				else -> emptyList()
			}
		}

	fun addHomeEntitlement(maxUsers: Int): List<Event> =
		mutate {
			when (state.status) {
				is Status.Active -> listOf(HomeEntitlementAdded(maxUsers))
				else -> emptyList()
			}
		}

	fun addHome(home: String): List<Event> =
		mutate {
			when (state.status) {
				is Status.Active -> if (state.entitled()) listOf(HomeAdded(home)) else emptyList()
				else -> emptyList()
			}
		}

}


