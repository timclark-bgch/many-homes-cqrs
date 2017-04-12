package connectedhome.honeycomb.home.storage.account

import connectedhome.honeycomb.home.domain.account.Created
import connectedhome.honeycomb.home.domain.account.Event
import connectedhome.honeycomb.home.domain.account.Suspended
import connectedhome.honeycomb.home.storage.*
import connectedhome.honeycomb.home.storage.protobuf.*
import honeycomb.home.events.account.Account

private val created = object : Converter<Created> {
	override fun read(bytes: ByteArray): Created? =
		fromProtobuf(bytes) { Created(Account.Created.parseFrom(bytes).owner) }

	override fun write(event: Created, id: String): Record {
		val bytes = Account.Created.newBuilder()
			.setAccount(id)
			.setOwner(event.owner)
			.build()
			.toByteArray()

		return Record(Key(id, proto()), bytes)
	}

	override fun proto(): String = Account.Created.getDescriptor().fullName
}

private val suspended = object : Converter<Suspended> {
	override fun read(bytes: ByteArray): Suspended? =
		fromProtobuf(bytes) { Suspended(Account.Suspended.parseFrom(bytes).reason) }

	override fun write(event: Suspended, id: String): Record {
		val bytes = Account.Suspended.newBuilder()
			.setAccount(id)
			.setReason(event.reason)
			.build()
			.toByteArray()

		return Record(Key(id, proto()), bytes)
	}

	override fun proto(): String = Account.Suspended.getDescriptor().fullName
}

class AccountStore(persistence: Persistence) : AbstractStore<Event>(persistence) {
	override val converter: StorageConverter<Event> = object : StorageConverter<Event> {
		override fun asRecord(id: String, event: Event): Record? =
			when (event) {
				is Created -> created.write(event, id)
				is Suspended -> suspended.write(event, id)
				else -> null
			}

		override fun fromRecord(record: Record): Event? {
			return when (record.key.proto) {
				created.proto() -> created.read(record.payload)
				suspended.proto() -> suspended.read(record.payload)
				else -> null
			}
		}
	}
}


