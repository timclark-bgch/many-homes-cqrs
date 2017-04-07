package connectedhome.honeycomb.home.storage

import connectedhome.honeycomb.home.domain.*
import honeycomb.home.events.Home
import java.util.*

data class Record(val id: String, val proto: String, val payload: ByteArray) {
	// Because this data class holds an array it needs to override equals and hashcode.
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other?.javaClass != javaClass) return false

		other as Record
		if (id != other.id) return false
		if (proto != other.proto) return false
		return Arrays.equals(payload, other.payload)
	}

	override fun hashCode(): Int {
		return Objects.hash(id, proto, Arrays.hashCode(payload))
	}
}

private fun asRecord(id: String, event: Event): Record? {
	val payload = asBytes(event, id)
	if (payload != null) {
		return when (event) {
			is Created -> Record(id, Home.Created.getDescriptor().fullName, payload)
			is Migrated -> Record(id, Home.Created.getDescriptor().fullName, payload)
			is Suspended -> Record(id, Home.Suspended.getDescriptor().fullName, payload)
			is Closed -> Record(id, Home.Closed.getDescriptor().fullName, payload)
			is Reactivated -> null
		}
	}

	return null
}

private fun asEvent(record: Record): Event? {
	return fromBytes(record.payload, record.proto)
}

interface Store {
	fun write(id: String, events: List<Event>): Boolean
	fun read(id: String): List<Event>
}

class SimpleStore : Store {
	private val store: MutableList<Record> = mutableListOf()

	override fun write(id: String, events: List<Event>): Boolean {
		return store.addAll(events.map { e -> asRecord(id, e) }.filterNotNull())
	}

	override fun read(id: String): List<Event> {
		return store
			.filter { r -> r.id == id }
			.map { r -> asEvent(r) }
			.filterNotNull()
	}
}



