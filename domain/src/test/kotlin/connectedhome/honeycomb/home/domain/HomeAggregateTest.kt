package connectedhome.honeycomb.home.domain

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class HomeAggregateTest {
	private val created = Created(Owner("owner"))

	@Test
	fun canBeSuspended() {
		val aggregate = HomeAggregate(HomeState(), listOf(created))

		val events = aggregate.suspend("test-suspension")

		assertTrue(events.isNotEmpty())
		assertTrue(events.size == 1)
		assertTrue(events.first() is Suspended)

		assertTrue(aggregate.suspend("already-suspended").isEmpty())
	}
	@Test
	fun canBeClosed() {
		val aggregate = HomeAggregate(HomeState(), listOf(created))

		val events = aggregate.close("test-suspension")

		assertTrue(events.isNotEmpty())
		assertTrue(events.size == 1)
		assertTrue(events.first() is Closed)

		assertTrue(aggregate.suspend("suspend-after-close").isEmpty())
		assertTrue(aggregate.close("close-after-close").isEmpty())
	}

	@Test
	fun canBeReactivatedWhenSuspended() {
		val aggregate = HomeAggregate(HomeState(), listOf(created, Suspended("test-reason")))

		val events = aggregate.reactivate("test-reactivate")

		assertTrue(events.isNotEmpty())
		assertTrue(events.size == 1)
		assertTrue(events.first() is Reactivated)

		assertTrue(aggregate.reactivate("second-reactivate").isEmpty())
	}

	@Test
	fun canBeReactivatedWhenClosed() {
		val aggregate = HomeAggregate(HomeState(), listOf(created, Closed("test-reason")))

		val events = aggregate.reactivate("test-reactivate")

		assertTrue(events.isNotEmpty())
		assertTrue(events.size == 1)
		assertTrue(events.first() is Reactivated)

		assertTrue(aggregate.reactivate("second-reactivate").isEmpty())
	}
}