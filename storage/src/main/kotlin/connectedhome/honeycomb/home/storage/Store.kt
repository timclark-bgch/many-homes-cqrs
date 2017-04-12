package connectedhome.honeycomb.home.storage


data class Key(val id: String, val proto: String)
class Record(val key: Key, val payload: ByteArray)

interface Store<T> {
	fun write(id: String, events: List<T>): Boolean
	fun read(id: String): List<T>
}

interface StorageConverter<T> {
	fun asRecord(id: String, event: T): Record?
	fun fromRecord(record: Record): T?
}

interface Persistence {
	fun write(records: List<Record>): Boolean
	fun read(id: String): List<Record>
}

abstract class AbstractStore<T>(private val persistence: Persistence) : Store<T> {
	protected abstract val converter: StorageConverter<T>

	override fun write(id: String, events: List<T>): Boolean =
		persistence.write(events.map { e -> converter.asRecord(id, e) }.filterNotNull())

	override fun read(id: String): List<T> {
		val events = mutableListOf<T>()
		persistence.read(id).map { r -> converter.fromRecord(r) }.forEach { r -> if (r != null) events.add(r) }

		return events
	}

}

class SimplePersistence(): Persistence {
	private val records = mutableListOf<Record>()

	override fun write(records: List<Record>): Boolean = this.records.addAll(records)

	override fun read(id: String): List<Record> = records.filter { r -> r.key.id != id }
}

//interface Store {
//	fun write(id: String, events: List<Event>): Boolean
//	fun read(id: String): List<Event>
//}
//
//private fun asRecord(id: String, event: Event): Record? {
//	val payload = asBytes(event, id)
//	if (payload != null) {
//		return when (event) {
//			is Created -> Record(id, Home.Created.getDescriptor().fullName, payload)
//			is Migrated -> Record(id, Home.Created.getDescriptor().fullName, payload)
//			is Suspended -> Record(id, Home.Suspended.getDescriptor().fullName, payload)
//			is Closed -> Record(id, Home.Closed.getDescriptor().fullName, payload)
//			is Reactivated -> null
//		}
//	}
//
//	return null
//}
//
//private fun asEvent(record: Record): Event? {
//	return fromBytes(record.payload, record.proto)
//}
//
//class SimpleStore : Store {
//	private val store: MutableList<Record> = mutableListOf()
//
//	override fun write(id: String, events: List<Event>): Boolean {
//		return store.addAll(events.map { e -> asRecord(id, e) }.filterNotNull())
//	}
//
//	override fun read(id: String): List<Event> {
//		return store
//			.filter { it.id == id }
//			.map { asEvent(it) }
//			.filterNotNull()
//	}
//}
//
//
//
