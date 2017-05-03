package connectedhome.honeycomb.home.storage

import honeycomb.home.events.EventHolder
import honeycomb.home.events.Listener


data class Key(val id: String, val version: Int, val proto: String)
class Record(val key: Key, val payload: ByteArray)

typealias StoredItems<T> = Versioned<List<T>>

interface Store<T> {
	fun write(id: String, version: Int, events: List<T>): Boolean
	fun read(id: String): StoredItems<T>
}

interface StorageConverter<T> {
	fun asRecord(id: String, version: Int, event: T): Record?
	fun fromRecord(record: Record): T?
}

typealias Records = Versioned<List<Record>>

interface Persistence {
	fun write(records: List<Record>): Boolean
	fun read(id: String): Records
}

abstract class AbstractStore<T>(private val persistence: Persistence) : Store<T> {
	protected abstract val converter: StorageConverter<T>

	override fun write(id: String, version: Int, events: List<T>): Boolean =
		persistence.write(events.withIndex().map { (index, value) -> converter.asRecord(id, version + index, value) }.filterNotNull())

	override fun read(id: String): StoredItems<T> {
		val events = mutableListOf<T>()
		val data = persistence.read(id)
		data.value.map { r -> converter.fromRecord(r) }.forEach { r -> if (r != null) events.add(r) }

		return StoredItems(data.version, events)
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

	override fun read(id: String): Records {
		val records = records.filter { r -> r.key.id == id }

		return Records(records.size, records)
	}
}