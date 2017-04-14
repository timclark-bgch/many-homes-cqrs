package connectedhome.honeycomb.home.commands

import connectedhome.honeycomb.home.domain.account.Created
import connectedhome.honeycomb.home.storage.account.AccountRepository

sealed class Response(val success: Boolean, val message: String?) {
	object Success : Response(true, null)
	class Failure(message: String) : Response(false, message)
}

sealed class Command
data class CreateAccount(val owner: String) : Command()
data class SuspendAccount(val account: String, val reason: String) : Command()
data class AddPropertyEntitlement(val account: String, val entitlement: String, val properties: Int, val users: Int) : Command()

class CommandHandler(private val accounts: AccountRepository) {
	fun handle(command: Command): Response {
		return when (command) {
			is CreateAccount -> createAccount(command)
			is SuspendAccount -> suspendAccount(command)
			is AddPropertyEntitlement -> addEntitlement(command)
			else -> Response.Failure("Unknown command")
		}
	}

	private fun createAccount(command: CreateAccount): Response {
		if (accounts.store(command.owner, 1, listOf(Created(command.owner)))) {
			return Response.Success
		}

		return Response.Failure("Unable to create account")
	}

	private fun suspendAccount(command: SuspendAccount): Response {
		val account = accounts.fetch(command.account)
		if (account != null) {
			if (accounts.store(command.account, 1, account.suspend(command.reason))) {
				return Response.Success
			}

			return Response.Failure("Unable to store account")
		}

		return Response.Failure("Unknown account")
	}

	private fun addEntitlement(command: AddPropertyEntitlement): Response {
		val account = accounts.fetch(command.account)
		if (account != null) {
			if (accounts.store(command.account, 1, account.addPropertyEntitlement(command.entitlement, command.properties, command.users))) {
				return Response.Success
			}
			return Response.Failure("Unable to store account")
		}

		return Response.Failure("Unknown account")
	}
}
