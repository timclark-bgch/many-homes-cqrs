package connectedhome.honeycomb.home.acceptance

import connectedhome.honeycomb.home.commands.CreateAccount
import connectedhome.honeycomb.home.commands.Response
import spock.lang.Specification

class OwnerTest extends Specification {

	def "an owner can be created and read"() {
		given:
		HomeService service = new HomeService()

		when:
		Response response = service.process(new CreateAccount("account", "owner"))

		then:
		response.success
	}

}
