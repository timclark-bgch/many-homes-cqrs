package connectedhome.honeycomb.home.storage

data class Versioned<out T>(val version: Int, val value: T)

interface Repository<out T, in E> {
	fun fetch(id: String): Versioned<T>?
	fun store(id: String, version: Int, events: List<E>): Boolean

}

abstract class AbstractRepository<out T, in E>(private val store: Store<E>) : Repository<T, E> {
	protected abstract fun build(events: List<E>): T

	override fun fetch(id: String): Versioned<T>? {
		val events = store.read(id)
		if (events.value.isNotEmpty()) {
			return Versioned(events.version, build(events.value))
		}

		return null
	}

	override fun store(id: String, version: Int, events: List<E>): Boolean {
		return store.write(id, version, events)
	}
}


