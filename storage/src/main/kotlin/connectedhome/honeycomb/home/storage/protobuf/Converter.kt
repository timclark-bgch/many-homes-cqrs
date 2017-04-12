package connectedhome.honeycomb.home.storage.protobuf

import com.google.protobuf.InvalidProtocolBufferException
import connectedhome.honeycomb.home.storage.Record


interface Converter<T> {
	fun read(bytes: ByteArray): T?
	fun write(event: T, id: String): Record
	fun proto(): String
}

fun <T> fromProtobuf(bytes: ByteArray, convert: (ByteArray) -> T): T? =
	try {
		convert(bytes)
	}
	catch(e: InvalidProtocolBufferException) {
		null
	}