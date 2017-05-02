package connectedhome.honeycomb.home.storage.owner

import connectedhome.honeycomb.home.domain.owner.Owner
import connectedhome.honeycomb.home.domain.owner.Event
import connectedhome.honeycomb.home.domain.owner.State
import connectedhome.honeycomb.home.storage.AbstractRepository

class OwnerRepository(store: OwnerStore): AbstractRepository<Owner, Event>(store) {
	override fun build(events: List<Event>): Owner {
		return Owner(State(), events)
	}
}

