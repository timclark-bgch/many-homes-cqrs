package connectedhome.honeycomb.home.storage.account

import connectedhome.honeycomb.home.domain.account.Created
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class AccountRepositoryTest	{
	@Test
	fun fetchesAccountIfStored()	{
		val store = AccountStore(TestPersistence(mutableListOf()))
		val repository = AccountRepository(store)

		assertTrue(store.write("123", listOf(Created("owner"))))

		assertNotNull(repository.fetch("123"))
	}

	@Test
	fun accountNotFound()	{
		val store = AccountStore(TestPersistence(mutableListOf()))
		val repository = AccountRepository(store)

		assertTrue(store.write("123", listOf(Created("owner"))))

		assertNull(repository.fetch("Unknown"))
	}

}