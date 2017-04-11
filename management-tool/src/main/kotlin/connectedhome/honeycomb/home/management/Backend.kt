package connectedhome.honeycomb.home.management

sealed class Either<out A, out B> {
	class Left<A>(val value: A) : Either<A, Nothing>()
	class Right<B>(val value: B) : Either<Nothing, B>()
}

fun addEntitlement(account: String, maxUsers: Int): Either<String, Boolean> {
	return Either.Left("Bang!")
}

fun removeEntitlement(account: String, entitlement: String): Boolean = false

fun addProperty(account: String, property: String): Boolean = false

fun removeProperty(account: String, property: String): Boolean = false

fun reactivate(account: String, reason: String): Boolean = false

fun close(account: String, reason: String): Boolean = false

fun suspend(account: String, reason: String): Boolean = false