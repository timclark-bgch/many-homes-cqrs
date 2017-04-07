package connectedhome.honeycomb.home.storage

import com.google.protobuf.InvalidProtocolBufferException
import connectedhome.honeycomb.home.domain.*
import honeycomb.home.events.Home

fun asBytes(event: Event, id: String): ByteArray? =
	when (event) {
		is Created -> created.write(event, id)
		is Migrated -> migrated.write(event, id)
		is Suspended -> suspended.write(event, id)
		is Closed -> closed.write(event, id)
		is Reactivated -> reactivated.write(event, id)
	}

fun fromBytes(bytes: ByteArray, proto: String): Event? =
	when (proto) {
		Home.Created.getDescriptor().fullName -> created.read(bytes)
		Home.Migrated.getDescriptor().fullName -> migrated.read(bytes)
		Home.Suspended.getDescriptor().fullName -> suspended.read(bytes)
		Home.Closed.getDescriptor().fullName -> closed.read(bytes)
		Home.Reactivated.getDescriptor().fullName -> reactivated.read(bytes)
		else -> null
	}

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

private val suspended = object : Converter<Suspended> {
	override fun read(bytes: ByteArray): Suspended? =
		try {
			Suspended(Home.Suspended.parseFrom(bytes).reason)
		}
		catch (e: InvalidProtocolBufferException) {
			null
		}

	override fun write(event: Suspended, id: String): ByteArray? =
		Home.Suspended.newBuilder()
			.setHome(id)
			.setReason(event.reason)
			.build()
			.toByteArray()
}

private val closed = object : Converter<Closed> {
	override fun read(bytes: ByteArray): Closed? =
		try {
			Closed(Home.Closed.parseFrom(bytes).reason)
		}
		catch (e: InvalidProtocolBufferException) {
			null
		}

	override fun write(event: Closed, id: String): ByteArray? =
		Home.Closed.newBuilder()
			.setHome(id)
			.setReason(event.reason)
			.build()
			.toByteArray()
}

private val reactivated = object : Converter<Reactivated> {
	override fun read(bytes: ByteArray): Reactivated? =
		try {
			Reactivated(Home.Reactivated.parseFrom(bytes).reason)
		}
		catch (e: InvalidProtocolBufferException) {
			null
		}

	override fun write(event: Reactivated, id: String): ByteArray? =
		Home.Reactivated.newBuilder()
			.setHome(id)
			.setReason(event.reason)
			.build()
			.toByteArray()
}


