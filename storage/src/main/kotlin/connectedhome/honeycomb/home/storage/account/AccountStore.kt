package connectedhome.honeycomb.home.storage.account

import connectedhome.honeycomb.home.domain.account.*
import connectedhome.honeycomb.home.storage.*
import connectedhome.honeycomb.home.storage.protobuf.*
import honeycomb.home.events.account.Account

private val created = object : Converter<Created> {
	override fun read(bytes: ByteArray): Created? =
		fromProtobuf(bytes) { Created(Account.Created.parseFrom(bytes).owner) }

	override fun write(event: Created, id: String): Record =
		Record(
			Key(id, proto()),
			Account.Created.newBuilder()
				.setAccount(id)
				.setOwner(event.owner)
				.build()
				.toByteArray()
		)


	override fun proto(): String = Account.Created.getDescriptor().fullName
}

private val entitlementAdded = object : Converter<PropertyEntitlementAdded> {
	override fun read(bytes: ByteArray): PropertyEntitlementAdded? =
		fromProtobuf(bytes) { PropertyEntitlementAdded(Account.PropertyEntitlementAdded.parseFrom(bytes).users) }

	override fun write(event: PropertyEntitlementAdded, id: String): Record =
		Record(
			Key(id, proto()),
			Account.PropertyEntitlementAdded.newBuilder()
				.setAccount(id)
				.setUsers(event.maxUsers)
				.build()
				.toByteArray()
		)

	override fun proto(): String = Account.PropertyEntitlementAdded.getDescriptor().fullName
}

private val propertyAdded = object : Converter<PropertyAdded> {
	override fun read(bytes: ByteArray): PropertyAdded? =
		fromProtobuf(bytes) { PropertyAdded(Account.PropertyAdded.parseFrom(bytes).property) }

	override fun write(event: PropertyAdded, id: String): Record =
		Record(
			Key(id, proto()),
			Account.PropertyAdded.newBuilder()
				.setAccount(id)
				.setProperty(event.property)
				.build()
				.toByteArray()
		)

	override fun proto(): String = Account.PropertyAdded.getDescriptor().fullName
}

private val suspended = object : Converter<Suspended> {
	override fun read(bytes: ByteArray): Suspended? =
		fromProtobuf(bytes) { Suspended(Account.Suspended.parseFrom(bytes).reason) }

	override fun write(event: Suspended, id: String): Record =
		Record(
			Key(id, proto()),
			Account.Suspended.newBuilder()
				.setAccount(id)
				.setReason(event.reason)
				.build()
				.toByteArray()
		)

	override fun proto(): String = Account.Suspended.getDescriptor().fullName
}

private val closed = object : Converter<Closed> {
	override fun read(bytes: ByteArray): Closed? =
		fromProtobuf(bytes) { Closed(Account.Closed.parseFrom(bytes).reason) }

	override fun write(event: Closed, id: String): Record =
		Record(
			Key(id, proto()),
			Account.Closed.newBuilder()
				.setAccount(id)
				.setReason(event.reason)
				.build()
				.toByteArray()
		)

	override fun proto(): String = Account.Closed.getDescriptor().fullName
}

private val reactivated = object : Converter<Reactivated> {
	override fun read(bytes: ByteArray): Reactivated? =
		fromProtobuf(bytes) { Reactivated(Account.Reactivated.parseFrom(bytes).reason) }

	override fun write(event: Reactivated, id: String): Record =
		Record(
			Key(id, proto()),
			Account.Reactivated.newBuilder()
				.setAccount(id)
				.setReason(event.reason)
				.build()
				.toByteArray()
		)

	override fun proto(): String = Account.Reactivated.getDescriptor().fullName
}

class AccountStore(persistence: Persistence) : AbstractStore<Event>(persistence) {
	override val converter: StorageConverter<Event> = object : StorageConverter<Event> {
		override fun asRecord(id: String, event: Event): Record? =
			when (event) {
				is Created -> created.write(event, id)
				is PropertyEntitlementAdded -> entitlementAdded.write(event, id)
				is PropertyAdded -> propertyAdded.write(event, id)
				is Suspended -> suspended.write(event, id)
				is Closed -> closed.write(event, id)
				is Reactivated -> reactivated.write(event, id)
				else -> null
			}

		override fun fromRecord(record: Record): Event? {
			return when (record.key.proto) {
				created.proto() -> created.read(record.payload)
				entitlementAdded.proto() -> entitlementAdded.read((record.payload))
				propertyAdded.proto() -> propertyAdded.read(record.payload)
				suspended.proto() -> suspended.read(record.payload)
				closed.proto() -> closed.read(record.payload)
				reactivated.proto() -> reactivated.read(record.payload)
				else -> null
			}
		}
	}
}


