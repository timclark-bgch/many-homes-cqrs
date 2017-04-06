package connectedhome.honeycomb.home.storage

import com.google.protobuf.InvalidProtocolBufferException
import connectedhome.honeycomb.home.domain.Created
import connectedhome.honeycomb.home.domain.Event
import connectedhome.honeycomb.home.domain.Migrated
import connectedhome.honeycomb.home.domain.Owner
import honeycomb.home.events.Home

private interface Converter<T> {
	fun read(bytes: ByteArray): T?
	fun write(event: T, id: String): ByteArray?
}

private val created = object : Converter<Created> {
	override fun read(bytes: ByteArray): Created? =
		try {
			val protobuf = Home.Created.parseFrom(bytes)
			Created(Owner(protobuf.owner))
		}
		catch (e: InvalidProtocolBufferException) {
			null
		}

	override fun write(event: Created, id: String): ByteArray? =
		Home.Created.newBuilder()
			.setHome(id)
			.setOwner(event.owner.id)
			.build()
			.toByteArray()

}

private val migrated = object : Converter<Migrated> {
	override fun read(bytes: ByteArray): Migrated? =
		try {
			val protobuf = Home.Migrated.parseFrom(bytes)
			Migrated(Owner(protobuf.owner))
		}
		catch (e: InvalidProtocolBufferException) {
			null
		}

	override fun write(event: Migrated, id: String): ByteArray? =
		Home.Migrated.newBuilder()
			.setHome(id)
			.setOwner(event.owner.id)
			.build()
			.toByteArray()
}

fun asBytes(event: Event, id: String): ByteArray? =
	when (event) {
		is Created -> created.write(event, id)
		is Migrated -> migrated.write(event, id)
		else -> null
	}


fun fromBytes(bytes: ByteArray, proto: String): Event? =
	when (proto) {
		Home.Created.getDescriptor().fullName -> created.read(bytes)
		Home.Migrated.getDescriptor().fullName -> migrated.read(bytes)
		else -> null
	}

