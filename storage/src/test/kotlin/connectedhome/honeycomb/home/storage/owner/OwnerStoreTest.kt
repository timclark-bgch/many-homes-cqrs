package connectedhome.honeycomb.home.storage.owner

import connectedhome.honeycomb.home.domain.owner.*
import connectedhome.honeycomb.home.storage.Persistence
import connectedhome.honeycomb.home.storage.Record
import connectedhome.honeycomb.home.storage.Records
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class OwnerStoreTest {

	@Test
	fun writesEvents() {
		val records = mutableListOf<Record>()
		val store = OwnerStore(TestPersistence(records))

		val events = listOf(
			Created("owner"),
			PropertyEntitlementAdded("entitlement", 1, 10),
			PropertyAdded("property"),
			Suspended("suspended"),
			Reactivated("reactivated"),
			Closed("closed")
		)

		assertTrue(store.write("123", 1, events))
		assertEquals(events.size, records.size)
	}

	@Test
	fun readsEvents() {
		val records = mutableListOf<Record>()
		val store = OwnerStore(TestPersistence(records))

		val id = "123"
		val owner = id
		val events = listOf(
			Created(owner),
			PropertyEntitlementAdded("entitlement", 2, 10),
			PropertyAdded("property"),
			Suspended("suspended"),
			Reactivated("reactivated"),
			Closed("closed")
		)

		assertTrue(store.write(id, 1, events))

		val stored = store.read(id).value
		assertEquals(6, stored.size)

		checkCreated(stored[0], owner)
		checkEntitlement(stored[1], 10)
		checkPropertyAdded(stored[2], "property")
		checkSuspended(stored[3], "suspended")
		checkReactivated(stored[4], "reactivated")
		checkClosed(stored[5], "closed")
	}

	fun checkCreated(event: Event, owner: String) {
		if (event is Created) {
			assertEquals(owner, event.owner)
		} else {
			fail("Event type is not Created")
		}
	}

	fun checkEntitlement(event: Event, users: Int) {
		if (event is PropertyEntitlementAdded) {
			assertEquals(users, event.maxUsers)
		} else {
			fail("Event type is not PropertyEntitlementAdded")
		}
	}

	fun checkPropertyAdded(event: Event, property: String) {
		if (event is PropertyAdded) {
			assertEquals(property, event.property)
		} else {
			fail("Event type is not PropertyAdded")
		}
	}

	fun checkSuspended(event: Event, reason: String) {
		if (event is Suspended) {
			assertEquals(reason, event.reason)
		} else {
			fail("Event type is not Suspended")
		}
	}

	fun checkReactivated(event: Event, reason: String) {
		if (event is Reactivated) {
			assertEquals(reason, event.reason)
		} else {
			fail("Event type is not Reactivated")
		}
	}

	fun checkClosed(event: Event, reason: String) {
		if (event is Closed) {
			assertEquals(reason, event.reason)
		} else {
			fail("Event type is not Closed")
		}
	}

}

internal class TestPersistence(val records: MutableList<Record>) : Persistence {
	override fun write(records: List<Record>): Boolean = this.records.addAll(records)

	override fun read(id: String): Records {
		val records = records.filter { r -> r.key.id == id }
		return Records(records.size, records)
	}
}