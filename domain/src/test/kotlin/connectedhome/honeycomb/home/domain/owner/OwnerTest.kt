package connectedhome.honeycomb.home.domain.owner

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class OwnerTest {
	@Test
	fun canBeSuspended()	{
		val owner = Owner(State(), listOf(Created("test")))

		val events = owner.suspend("test-reason")

		assertTrue(events.isNotEmpty())
		assertTrue(events.size == 1)
		assertTrue(events.first() is Suspended)

		assertTrue(owner.suspend("second-suspend-does-nothing").isEmpty())
	}

	@Test
	fun canBeClosed()	{
		val owner = Owner(State(), listOf(Created("test"), Suspended("test")))

		val events = owner.close("test-reason")

		assertTrue(events.isNotEmpty())
		assertTrue(events.size == 1)
		assertTrue(events.first() is Closed)

		assertTrue(owner.close("second-close-does-nothing").isEmpty())
	}

	@Test
	fun canBeReactivated()	{
		val owner = Owner(State(), listOf(Created("test"), Closed("test")))

		val events = owner.reactivate("test-reason")

		assertTrue(events.isNotEmpty())
		assertTrue(events.size == 1)
		assertTrue(events.first() is Reactivated)

		assertTrue(owner.reactivate("second-reactivate-does-nothing").isEmpty())
	}

	@Test
	fun entitlementCannotBeAddedIfAccountClosed()	{
		val owner = Owner(State(), listOf(Created("test"), Closed("test")))

		val events = owner.addPropertyEntitlement("test", 2, 10)

		assertTrue(events.isEmpty())
	}

	@Test
	fun entitlementCannotBeAddedIfAccountSuspended()	{
		val owner = Owner(State(), listOf(Created("test"), Suspended("test")))

		val events = owner.addPropertyEntitlement("test", 2, 10)

		assertTrue(events.isEmpty())
	}

	@Test
	fun entitlementCanBeAdded()	{
		val owner = Owner(State(), listOf(Created("test")))

		val events = owner.addPropertyEntitlement("test", 2, 10)

		assertTrue(events.isNotEmpty())
		assertTrue(events.size == 1)
		assertTrue(events.first() is PropertyEntitlementAdded)

		assertTrue(owner.addPropertyEntitlement("test", 1, 5).isNotEmpty())
		assertTrue(owner.addPropertyEntitlement("test", 1, 50).isNotEmpty())
	}

	@Test
	fun homeCannotBeAddedWithoutEntitlement()	{
		val owner = Owner(State(), listOf(Created("test")))

		val events = owner.addProperty("test-property")

		assertTrue(events.isEmpty())
	}

	@Test
	fun homeCannotBeAddedWhenAccountSuspended()	{
		val owner = Owner(State(),
			listOf(Created("test"), PropertyEntitlementAdded("test", 2, 10), Suspended("test")))

		val events = owner.addProperty("test-property")

		assertTrue(events.isEmpty())
	}

	@Test
	fun homeCannotBeAddedWhenAccountClosed()	{
		val owner = Owner(State(),
			listOf(Created("test"), PropertyEntitlementAdded("test", 2, 10), Closed("test")))

		val events = owner.addProperty("test-property")

		assertTrue(events.isEmpty())
	}

	@Test
	fun homeCanBeAddedWithEntitlement()	{
		val owner = Owner(State(), listOf(Created("test"), PropertyEntitlementAdded("test", 1, 10)))

		val events = owner.addProperty("test-property")

		assertTrue(events.isNotEmpty())
		assertTrue(events.size == 1)
		assertTrue(events.first() is PropertyAdded)

		assertTrue(owner.addProperty("another-property").isEmpty())
	}

}


