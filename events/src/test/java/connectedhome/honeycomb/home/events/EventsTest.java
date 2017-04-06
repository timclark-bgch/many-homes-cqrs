package connectedhome.honeycomb.home.events;

import honeycomb.home.events.Home;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

final class EventsTest {

	@Test
	void testCreated() {
		final String id = "test_id";
		final String user = "test_user";
		final Home.Created created = Home.Created.newBuilder().setHome(id).setOwner(user).build();

		assertThat(created).hasFieldOrPropertyWithValue("home", id);
		assertThat(created).hasFieldOrPropertyWithValue("owner", user);
	}

	@Test
	void testMigrated() {
		final String id = "test_id";
		final String user = "test_user";
		final Home.Migrated migrated = Home.Migrated.newBuilder().setHome(id).setOwner(user).build();

		assertThat(migrated).hasFieldOrPropertyWithValue("home", id);
		assertThat(migrated).hasFieldOrPropertyWithValue("owner", user);
	}

	@Test
	void testSuspended()	{
		final String id = "test_id";
		final String reason = "test_reason";

		final Home.Suspended suspended = Home.Suspended.newBuilder().setHome(id).setReason(reason).build();

		assertThat(suspended).hasFieldOrPropertyWithValue("home", id);
		assertThat(suspended).hasFieldOrPropertyWithValue("reason", reason);
	}

	@Test
	void testClosed()	{
		final String id = "test_id";
		final String reason = "test_reason";

		final Home.Closed closed = Home.Closed.newBuilder().setHome(id).setReason(reason).build();

		assertThat(closed).hasFieldOrPropertyWithValue("home", id);
		assertThat(closed).hasFieldOrPropertyWithValue("reason", reason);
	}
}
