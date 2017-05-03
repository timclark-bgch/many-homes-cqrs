package connectedhome.honeycomb.home.storage.owner

import connectedhome.honeycomb.home.domain.owner.Created
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class OwnerRepositoryTest {
	@Test
	fun fetchesOwnerIfStored()	{
		val store = OwnerStore(TestPersistence(mutableListOf()))
		val repository = OwnerRepository(store)

		assertTrue(store.write("123", listOf(Created("owner"))))

		assertNotNull(repository.fetch("123"))
	}

	@Test
	fun ownerNotFound()	{
		val store = OwnerStore(TestPersistence(mutableListOf()))
		val repository = OwnerRepository(store)

		assertTrue(store.write("123", listOf(Created("owner"))))

		assertNull(repository.fetch("Unknown"))
	}

}