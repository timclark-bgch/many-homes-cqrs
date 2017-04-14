package connectedhome.honeycomb.home.storage

import honeycomb.home.events.EventHolder
import honeycomb.home.events.Listener


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

class SimplePersistence(private val listener: Listener) : Persistence {
	private val records = mutableListOf<Record>()

	override fun write(records: List<Record>): Boolean {
		if (this.records.addAll(records)) {
			records.forEach { r -> listener.event(EventHolder(r.key.proto, r.payload)) }
			return true
		}

		return false
	}

	override fun read(id: String): List<Record> = records.filter { r -> r.key.id == id }
}