package connectedhome.honeycomb.home.acceptance

import connectedhome.honeycomb.home.commands.AddPropertyEntitlement
import connectedhome.honeycomb.home.commands.Command
import connectedhome.honeycomb.home.commands.CreateAccount
import connectedhome.honeycomb.home.commands.Response
import connectedhome.honeycomb.home.queries.simple.Entitlement
import connectedhome.honeycomb.home.queries.simple.Owner
import spock.lang.Specification

class OwnerTest extends Specification {

	def "An owner can be created and read"() {
		given:
		HomeService service = new HomeService()
		def userId = "user-id"

		when:
		Response response = service.process(new CreateAccount(userId))
		Owner owner = service.owner(userId)

		then:
		response.success
		owner.user == userId
		owner.entitlements.isEmpty()
		owner.homes.isEmpty()
	}

	def "An owner can have entitlements"() {
		given:
		HomeService service = new HomeService()
		def userId = "user-id"

		when:
		List<Command> commands = [
				new CreateAccount(userId),
				new AddPropertyEntitlement(userId, "entitlement", 2, 9)
		]

		List<Response> responses = commands.collect { service.process(it) }
		Owner owner = service.owner(userId)

		then:
		responses.every { it.success }
		owner.user == userId
		owner.entitlements == [new Entitlement("entitlement", 2, 9)]
		owner.homes.isEmpty()
	}

}
