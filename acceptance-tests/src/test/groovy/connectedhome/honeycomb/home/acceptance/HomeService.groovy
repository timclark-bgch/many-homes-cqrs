package connectedhome.honeycomb.home.acceptance

import connectedhome.honeycomb.home.commands.Command
import connectedhome.honeycomb.home.commands.CommandHandler
import connectedhome.honeycomb.home.commands.Response
import connectedhome.honeycomb.home.storage.SimplePersistence
import connectedhome.honeycomb.home.storage.account.AccountRepository
import connectedhome.honeycomb.home.storage.account.AccountStore


final class HomeService {
	private final SimplePersistence persistence = new SimplePersistence()
	private final AccountStore store = new AccountStore(persistence)
	private final AccountRepository repository = new AccountRepository(store)
	private final CommandHandler commands = new CommandHandler(repository)

	Response process(Command command) {
		return commands.handle(command)
	}
}
