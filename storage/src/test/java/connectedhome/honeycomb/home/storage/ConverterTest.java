package connectedhome.honeycomb.home.storage;

import com.google.protobuf.InvalidProtocolBufferException;
import connectedhome.honeycomb.home.domain.Created;
import connectedhome.honeycomb.home.domain.Event;
import connectedhome.honeycomb.home.domain.Migrated;
import connectedhome.honeycomb.home.domain.Owner;
import honeycomb.home.events.Home;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

final class ConverterTest {
	@Test
	void createdAsBytes() throws InvalidProtocolBufferException {
		final Owner owner = new Owner("owner");
		final String id = "test-id";
		final byte[] bytes = ConverterKt.asBytes(new Created(owner), id);

		assertThat(bytes).isNotNull();

		final Home.Created created = Home.Created.parseFrom(bytes);
		assertThat(created).hasFieldOrPropertyWithValue("home", id);
		assertThat(created).hasFieldOrPropertyWithValue("owner", owner.getId());
	}

	@Test
	void createdFromBytes() {
		final String home = "test-home";
		final String owner = "test-owner";
		final Home.Created original = Home.Created.newBuilder().setHome(home).setOwner(owner).build();

		final Event event = ConverterKt.fromBytes(original.toByteArray(), Home.Created.getDescriptor().getFullName());

		assertThat(event).isNotNull();
		assertThat(event).isInstanceOf(Created.class);
		assertThat(event).hasFieldOrPropertyWithValue("owner", new Owner(owner));
	}

	@Test
	void migratedAsBytes() throws InvalidProtocolBufferException {
		final Owner owner = new Owner("owner");
		final String id = "test-id";
		final byte[] bytes = ConverterKt.asBytes(new Migrated(owner), id);

		assertThat(bytes).isNotNull();

		final Home.Migrated created = Home.Migrated.parseFrom(bytes);
		assertThat(created).hasFieldOrPropertyWithValue("home", id);
		assertThat(created).hasFieldOrPropertyWithValue("owner", owner.getId());
	}

	@Test
	void migratedFromBytes() {
		final String home = "test-home";
		final String owner = "test-owner";
		final Home.Migrated original = Home.Migrated.newBuilder().setHome(home).setOwner(owner).build();

		final Event event = ConverterKt.fromBytes(original.toByteArray(), Home.Migrated.getDescriptor().getFullName());

		assertThat(event).isNotNull();
		assertThat(event).isInstanceOf(Migrated.class);
		assertThat(event).hasFieldOrPropertyWithValue("owner", new Owner(owner));
	}

	@Test
	void badDataFails() {
		final Event event = ConverterKt.fromBytes("bad-bytes".getBytes(), Home.Created.getDescriptor().getFullName());

		assertThat(event).isNull();
	}

	@Test
	void unknownProtoFails() {
		final Event event = ConverterKt.fromBytes("bad-bytes".getBytes(), "I am an unknown proto");

		assertThat(event).isNull();
	}
}
