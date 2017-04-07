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

		val events = account.addHomeEntitlement(10)

		assertTrue(events.isEmpty())
	}

	@Test
	fun entitlementCannotBeAddedIfAccountSuspended()	{
		val account = Account(State(), listOf(Created("test"), Suspended("test")))

		val events = account.addHomeEntitlement(10)

		assertTrue(events.isEmpty())
	}

	@Test
	fun entitlementCanBeAdded()	{
		val account = Account(State(), listOf(Created("test")))

		val events = account.addHomeEntitlement(10)

		assertTrue(events.isNotEmpty())
		assertTrue(events.size == 1)
		assertTrue(events.first() is HomeEntitlementAdded)

		assertTrue(account.addHomeEntitlement(5).isNotEmpty())
		assertTrue(account.addHomeEntitlement(50).isNotEmpty())
	}

}


