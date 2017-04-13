package connectedhome.honeycomb.home.queries.simple

import honeycomb.home.events.EventHolder
import honeycomb.home.events.Home
import honeycomb.home.events.Listener
import honeycomb.home.events.account.Account

data class Owner(val user: String)

class OwnerView(): Listener {
	private val owners = mutableMapOf<String, Owner>()

	override fun event(holder: EventHolder?) {
		if(holder != null)	{
			when(holder.descriptor) {
				Account.Created.getDescriptor().fullName -> accountCreated(holder.data)
			}
		}
	}

	private fun accountCreated(data: ByteArray?) {
		if(data != null)	{
			val created = Account.Created.parseFrom(data)
			owners.putIfAbsent(created.account, Owner(created.owner))
		}
	}

	fun owner(id: String): Owner? = owners[id]
}
