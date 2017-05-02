package connectedhome.honeycomb.home.domain.owner

import connectedhome.honeycomb.home.domain.Aggregate

sealed class Event
data class Created(val owner: String) : Event()
data class Suspended(val reason: String) : Event()
data class Reactivated(val reason: String) : Event()
data class Closed(val reason: String) : Event()
data class PropertyEntitlementAdded(val label: String, val properties: Int, val maxUsers: Int) : Event()
data class PropertyAdded(val property: String) : Event()

sealed class Status {
	class Active : Status()
	class Suspended : Status()
	class Closed : Status()
}

data class HomeEntitlement(val maxUsers: Int)

data class State(
	val status: Status = Status.Active(),
	val entitlements: List<HomeEntitlement> = emptyList(),
	val properties: List<String> = emptyList())

private fun State.entitled(): Boolean =
	this.entitlements.size > this.properties.size

class Owner(state: State, events: List<Event>) : Aggregate<State, Event>(state, events) {
	override fun apply(event: Event): State = when (event) {
		is Created -> state
		is Suspended -> state.copy(status = Status.Suspended())
		is Reactivated -> state.copy(status = Status.Active())
		is Closed -> state.copy(status = Status.Closed())
		is PropertyEntitlementAdded -> state.copy(entitlements = state.entitlements + HomeEntitlement(event.maxUsers))
		is PropertyAdded -> state.copy(properties = state.properties + event.property)
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

	fun addPropertyEntitlement(label: String, properties: Int, maxUsers: Int): List<Event> =
		mutate {
			when (state.status) {
				is Status.Active -> listOf(PropertyEntitlementAdded(label, properties, maxUsers))
				else -> emptyList()
			}
		}

	fun addProperty(home: String): List<Event> =
		mutate {
			when (state.status) {
				is Status.Active -> if (state.entitled()) listOf(PropertyAdded(home)) else emptyList()
				else -> emptyList()
			}
		}

}


