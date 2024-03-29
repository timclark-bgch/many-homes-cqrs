package connectedhome.honeycomb.home.storage.owner

import connectedhome.honeycomb.home.domain.owner.*
import connectedhome.honeycomb.home.storage.*
import connectedhome.honeycomb.home.storage.protobuf.*
import honeycomb.home.events.owner.Owner

class OwnerStore(persistence: Persistence) : AbstractStore<Event>(persistence) {
	override val converter: StorageConverter<Event> = object : StorageConverter<Event> {
		override fun asRecord(id: String, version: Int, event: Event): Record? =
			when (event) {
				is Created -> created.write(event, version, id)
				is PropertyEntitlementAdded -> entitlementAdded.write(event, version, id)
				is PropertyAdded -> propertyAdded.write(event, version, id)
				is Suspended -> suspended.write(event, version, id)
				is Closed -> closed.write(event, version, id)
				is Reactivated -> reactivated.write(event, version, id)
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

private val created = object : Converter<Created> {
	override fun read(bytes: ByteArray): Created? =
		fromProtobuf(bytes) { Created(Owner.Created.parseFrom(bytes).owner) }

	override fun write(event: Created, version: Int, id: String): Record =
		Record(
			Key(id, version, proto()),
			Owner.Created.newBuilder()
				.setOwner(id)
				.build()
				.toByteArray()
		)


	override fun proto(): String = Owner.Created.getDescriptor().fullName
}

private val entitlementAdded = object : Converter<PropertyEntitlementAdded> {
	override fun read(bytes: ByteArray): PropertyEntitlementAdded? =
		fromProtobuf(bytes) {
			val entitlement = Owner.PropertyEntitlementAdded.parseFrom(bytes)
			PropertyEntitlementAdded(entitlement.label, entitlement.properties, entitlement.users)
		}

	override fun write(event: PropertyEntitlementAdded, version: Int, id: String): Record =
		Record(
			Key(id, version, proto()),
			Owner.PropertyEntitlementAdded.newBuilder()
				.setOwner(id)
				.setLabel(event.label)
				.setProperties(event.properties)
				.setUsers(event.maxUsers)
				.build()
				.toByteArray()
		)

	override fun proto(): String = Owner.PropertyEntitlementAdded.getDescriptor().fullName
}

private val propertyAdded = object : Converter<PropertyAdded> {
	override fun read(bytes: ByteArray): PropertyAdded? =
		fromProtobuf(bytes) { PropertyAdded(Owner.PropertyAdded.parseFrom(bytes).property) }

	override fun write(event: PropertyAdded, version: Int, id: String): Record =
		Record(
			Key(id, version, proto()),
			Owner.PropertyAdded.newBuilder()
				.setOwner(id)
				.setProperty(event.property)
				.build()
				.toByteArray()
		)

	override fun proto(): String = Owner.PropertyAdded.getDescriptor().fullName
}

private val suspended = object : Converter<Suspended> {
	override fun read(bytes: ByteArray): Suspended? =
		fromProtobuf(bytes) { Suspended(Owner.Suspended.parseFrom(bytes).reason) }

	override fun write(event: Suspended, version: Int, id: String): Record =
		Record(
			Key(id, version, proto()),
			Owner.Suspended.newBuilder()
				.setOwner(id)
				.setReason(event.reason)
				.build()
				.toByteArray()
		)

	override fun proto(): String = Owner.Suspended.getDescriptor().fullName
}

private val closed = object : Converter<Closed> {
	override fun read(bytes: ByteArray): Closed? =
		fromProtobuf(bytes) { Closed(Owner.Closed.parseFrom(bytes).reason) }

	override fun write(event: Closed, version: Int, id: String): Record =
		Record(
			Key(id, version, proto()),
			Owner.Closed.newBuilder()
				.setOwner(id)
				.setReason(event.reason)
				.build()
				.toByteArray()
		)

	override fun proto(): String = Owner.Closed.getDescriptor().fullName
}

private val reactivated = object : Converter<Reactivated> {
	override fun read(bytes: ByteArray): Reactivated? =
		fromProtobuf(bytes) { Reactivated(Owner.Reactivated.parseFrom(bytes).reason) }

	override fun write(event: Reactivated, version: Int, id: String): Record =
		Record(
			Key(id, version, proto()),
			Owner.Reactivated.newBuilder()
				.setOwner(id)
				.setReason(event.reason)
				.build()
				.toByteArray()
		)

	override fun proto(): String = Owner.Reactivated.getDescriptor().fullName
}




