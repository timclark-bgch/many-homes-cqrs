package connectedhome.honeycomb.home.domain.account

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class AccountTest {
	@Test
	fun canBeSuspended()	{
		val account = Account(State(), listOf(Created("test")))

		val events = account.suspend("test-reason")

		assertTrue(events.isNotEmpty())
		assertTrue(events.size == 1)
		assertTrue(events.first() is Suspended)

		assertTrue(account.suspend("second-suspend-does-nothing").isEmpty())
	}

	@Test
	fun canBeClosed()	{
		val account = Account(State(), listOf(Created("test"), Suspended("test")))

		val events = account.close("test-reason")

		assertTrue(events.isNotEmpty())
		assertTrue(events.size == 1)
		assertTrue(events.first() is Closed)

		assertTrue(account.close("second-close-does-nothing").isEmpty())
	}

	@Test
	fun canBeReactivated()	{
		val account = Account(State(), listOf(Created("test"), Closed("test")))

		val events = account.reactivate("test-reason")

		assertTrue(events.isNotEmpty())
		assertTrue(events.size == 1)
		assertTrue(events.first() is Reactivated)

		assertTrue(account.reactivate("second-reactivate-does-nothing").isEmpty())
	}

	@Test
	fun entitlementCannotBeAddedIfAccountClosed()	{
		val account = Account(State(), listOf(Created("test"), Closed("test")))

		val events = account.addPropertyEntitlement(10)

		assertTrue(events.isEmpty())
	}

	@Test
	fun entitlementCannotBeAddedIfAccountSuspended()	{
		val account = Account(State(), listOf(Created("test"), Suspended("test")))

		val events = account.addPropertyEntitlement(10)

		assertTrue(events.isEmpty())
	}

	@Test
	fun entitlementCanBeAdded()	{
		val account = Account(State(), listOf(Created("test")))

		val events = account.addPropertyEntitlement(10)

		assertTrue(events.isNotEmpty())
		assertTrue(events.size == 1)
		assertTrue(events.first() is PropertyEntitlementAdded)

		assertTrue(account.addPropertyEntitlement(5).isNotEmpty())
		assertTrue(account.addPropertyEntitlement(50).isNotEmpty())
	}

	@Test
	fun homeCannotBeAddedWithoutEntitlement()	{
		val account = Account(State(), listOf(Created("test")))

		val events = account.addProperty("test-home")

		assertTrue(events.isEmpty())
	}

	@Test
	fun homeCannotBeAddedWhenAccountSuspended()	{
		val account = Account(State(),
			listOf(Created("test"), PropertyEntitlementAdded(10), Suspended("test")))

		val events = account.addProperty("test-home")

		assertTrue(events.isEmpty())
	}

	@Test
	fun homeCannotBeAddedWhenAccountClosed()	{
		val account = Account(State(),
			listOf(Created("test"), PropertyEntitlementAdded(10), Closed("test")))

		val events = account.addProperty("test-home")

		assertTrue(events.isEmpty())
	}

	@Test
	fun homeCanBeAddedWithEntitlement()	{
		val account = Account(State(), listOf(Created("test"), PropertyEntitlementAdded(10)))

		val events = account.addProperty("test-home")

		assertTrue(events.isNotEmpty())
		assertTrue(events.size == 1)
		assertTrue(events.first() is PropertyAdded)

		assertTrue(account.addProperty("another-home").isEmpty())
	}

}


