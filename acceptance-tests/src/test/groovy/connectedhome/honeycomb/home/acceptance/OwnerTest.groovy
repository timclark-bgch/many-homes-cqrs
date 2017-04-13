package connectedhome.honeycomb.home.acceptance

import connectedhome.honeycomb.home.commands.CreateAccount
import connectedhome.honeycomb.home.commands.Response
import connectedhome.honeycomb.home.queries.simple.Owner
import spock.lang.Specification

class OwnerTest extends Specification {

	def "an owner can be created and read"() {
		given:
		HomeService service = new HomeService()
		def user = "owner"

		when:
		Response response = service.process(new CreateAccount("account", user))
		Owner owner = service.owner("account")

		then:
		response.success
		owner.user == user
	}

}
