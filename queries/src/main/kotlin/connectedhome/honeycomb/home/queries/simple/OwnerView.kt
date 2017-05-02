package connectedhome.honeycomb.home.queries.simple

import honeycomb.home.events.EventHolder
import honeycomb.home.events.Listener
import honeycomb.home.events.owner.Owner

data class Home(val home: String, val maxUsers: Int, val users: List<String>)
data class Entitlement(val entitlement: String, val maxProperties: Int, val usersPerProperty: Int)
data class OwnerRecord(val user: String, val homes: List<Home>, val entitlements: List<Entitlement>)

class OwnerView : Listener {
	private val owners = mutableMapOf<String, OwnerRecord>()

	override fun event(holder: EventHolder?) {
		if (holder != null) {
			when {
				holder.descriptor == Owner.Created.getDescriptor().fullName -> accountCreated(holder.data)
				holder.descriptor == Owner.PropertyEntitlementAdded.getDescriptor().fullName -> entitlementAdded(holder.data)
			}
		}
	}

	private fun entitlementAdded(data: ByteArray?) {
		if (data != null) {
			val entitlement = Owner.PropertyEntitlementAdded.parseFrom(data)
			val owner = owners[entitlement.owner]
			if (owner != null) {
				owners[entitlement.owner] =
					owner.copy(entitlements = owner.entitlements + Entitlement(entitlement.label, entitlement.properties, entitlement.users))
			}
		}
	}

	private fun accountCreated(data: ByteArray?) {
		if (data != null) {
			val created = Owner.Created.parseFrom(data)
			owners.putIfAbsent(created.owner, OwnerRecord(created.owner, emptyList(), emptyList()))
		}
	}

	fun owner(id: String): OwnerRecord? = owners[id]
}
