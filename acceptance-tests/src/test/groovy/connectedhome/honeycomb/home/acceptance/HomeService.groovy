package connectedhome.honeycomb.home.acceptance

import connectedhome.honeycomb.home.commands.Command
import connectedhome.honeycomb.home.commands.CommandHandler
import connectedhome.honeycomb.home.commands.Response
import connectedhome.honeycomb.home.queries.simple.Owner
import connectedhome.honeycomb.home.queries.simple.OwnerView
import connectedhome.honeycomb.home.storage.SimplePersistence
import connectedhome.honeycomb.home.storage.account.AccountRepository
import connectedhome.honeycomb.home.storage.account.AccountStore
import honeycomb.home.events.EventHolder
import honeycomb.home.events.Listener


final class HomeService {
	private final OwnerView ownerView = new OwnerView()

	private final Listener listener = new Listener() {
		@Override
		void event(EventHolder holder) {
			ownerView.event(holder)
		}
	}

	private final SimplePersistence persistence = new SimplePersistence(listener)
	private final AccountStore store = new AccountStore(persistence)
	private final AccountRepository repository = new AccountRepository(store)
	private final CommandHandler commands = new CommandHandler(repository)

	Response process(Command command) {
		return commands.handle(command)
	}

	Owner owner(final String id) {
		return ownerView.owner(id)
	}
}
