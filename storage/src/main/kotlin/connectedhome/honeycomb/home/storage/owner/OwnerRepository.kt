package connectedhome.honeycomb.home.storage.owner

import connectedhome.honeycomb.home.domain.account.Account
import connectedhome.honeycomb.home.domain.account.Event
import connectedhome.honeycomb.home.domain.account.State
import connectedhome.honeycomb.home.storage.AbstractRepository

class OwnerRepository(store: OwnerStore): AbstractRepository<Account, Event>(store) {
	override fun build(events: List<Event>): Account {
		return Account(State(), events)
	}
}

