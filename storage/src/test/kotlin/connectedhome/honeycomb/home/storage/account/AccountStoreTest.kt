package connectedhome.honeycomb.home.storage.account

import connectedhome.honeycomb.home.domain.account.Created
import connectedhome.honeycomb.home.domain.account.Event
import connectedhome.honeycomb.home.domain.account.Suspended
import connectedhome.honeycomb.home.storage.Persistence
import connectedhome.honeycomb.home.storage.Record
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class AccountStoreTest {

	@Test
	fun writesEvents() {
		val records = mutableListOf<Record>()
		val store = AccountStore(TestPersistence(records))

		val events = listOf(Created("owner"), Suspended("reason"))
		assertTrue(store.write("123", events))
		assertEquals(events.size, records.size)
	}

	@Test
	fun readsEvents() {
		val records = mutableListOf<Record>()
		val store = AccountStore(TestPersistence(records))

		val id = "123"
		val owner = "owner"
		val reason = "reason"
		assertTrue(store.write(id, listOf(Created(owner), Suspended(reason))))

		val events = store.read(id)
		assertEquals(2, events.size)

		checkCreated(events[0], owner)
		checkSuspended(events[1], reason)
	}

	fun checkCreated(event: Event, owner: String) {
		if (event is Created) {
			assertEquals(owner, event.owner)
		} else {
			fail("Event type is not Created")
		}
	}

	fun checkSuspended(event: Event, reason: String) {
		if (event is Suspended) {
			assertEquals(reason, event.reason)
		} else {
			fail("Event type is not Suspended")
		}
	}
}

internal class TestPersistence(val records: MutableList<Record>) : Persistence {
	override fun write(records: List<Record>): Boolean = this.records.addAll(records)

	override fun read(id: String): List<Record> = records.filter { r -> r.key.id == id }

}