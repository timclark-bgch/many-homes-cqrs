package connectedhome.honeycomb.home.storage

interface Repository<out T, in E> {
	fun fetch(id: String): T?
	fun store(id: String, version: Int, events: List<E>): Boolean
}

abstract class AbstractRepository<out T, in E>(private val store: Store<E>) : Repository<T, E> {
	protected abstract fun build(events: List<E>): T

	override fun fetch(id: String): T? {
		val events = store.read(id)
		if (events.isNotEmpty()) {
			return build(events)
		}
		return null
	}

	override fun store(id: String, version: Int, events: List<E>): Boolean {
		return store.write(id, events)
	}
}


