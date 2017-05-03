package connectedhome.honeycomb.home.commands

import connectedhome.honeycomb.home.domain.owner.Created
import connectedhome.honeycomb.home.storage.owner.OwnerRepository

sealed class Response(val success: Boolean, val message: String?) {
	object Success : Response(true, null)
	class Failure(message: String) : Response(false, message)
}

sealed class Command
data class CreateOwner(val owner: String) : Command()
data class SuspendOwner(val owner: String, val reason: String) : Command()
data class AddPropertyEntitlement(val owner: String, val entitlement: String, val properties: Int, val users: Int) : Command()

class CommandHandler(private val owners: OwnerRepository) {
	fun handle(command: Command): Response {
		return when (command) {
			is CreateOwner -> createOwner(command)
			is SuspendOwner -> suspendOwner(command)
			is AddPropertyEntitlement -> addEntitlement(command)
			else -> Response.Failure("Unknown command")
		}
	}

	private fun createOwner(command: CreateOwner): Response {
		if (owners.store(command.owner, 1, listOf(Created(command.owner)))) {
			return Response.Success
		}

		return Response.Failure("Unable to create owner")
	}

	private fun suspendOwner(command: SuspendOwner): Response {
		val owner = owners.fetch(command.owner)
		if (owner != null) {
			if (owners.store(command.owner, owner.version, owner.value.suspend(command.reason))) {
				return Response.Success
			}

			return Response.Failure("Unable to store owner")
		}

		return Response.Failure("Unknown owner")
	}

	private fun addEntitlement(command: AddPropertyEntitlement): Response {
		val owner = owners.fetch(command.owner)
		if (owner != null) {
			if (owners.store(command.owner, owner.version, owner.value.addPropertyEntitlement(command.entitlement, command.properties, command.users))) {
				return Response.Success
			}
			return Response.Failure("Unable to store owner")
		}

		return Response.Failure("Unknown owner")
	}
}
