package connectedhome.honeycomb.home.commands

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class CommandHandlerTest {
	@Test
	fun nonsense() {
		assertTrue(CreateAccount("owner") is Command)
	}
}