package connectedhome.honeycomb.home.queries.simple

import honeycomb.home.events.EventHolder
import honeycomb.home.events.Listener
import honeycomb.home.events.account.Account

data class Home(val home: String, val maxUsers: Int, val users: List<String>)
data class Entitlement(val entitlement: String, val maxProperties: Int, val usersPerProperty: Int)
data class Owner(val user: String, val homes: List<Home>, val entitlements: List<Entitlement>)

class OwnerView : Listener {
	private val owners = mutableMapOf<String, Owner>()

	override fun event(holder: EventHolder?) {
		if (holder != null) {
			when {
				holder.descriptor == Account.Created.getDescriptor().fullName -> accountCreated(holder.data)
				holder.descriptor == Account.PropertyEntitlementAdded.getDescriptor().fullName -> entitlementAdded(holder.data)
			}
		}
	}

	private fun entitlementAdded(data: ByteArray?) {
		if (data != null) {
			val entitlement = Account.PropertyEntitlementAdded.parseFrom(data)
			val owner = owners[entitlement.account]
			if (owner != null) {
				owners[entitlement.account] =
					owner.copy(entitlements = owner.entitlements + Entitlement(entitlement.label, entitlement.properties, entitlement.users))
			}
		}
	}

	private fun accountCreated(data: ByteArray?) {
		if (data != null) {
			val created = Account.Created.parseFrom(data)
			owners.putIfAbsent(created.account, Owner(created.owner, emptyList(), emptyList()))
		}
	}

	fun owner(id: String): Owner? = owners[id]
}
