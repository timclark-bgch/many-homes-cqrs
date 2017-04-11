package connectedhome.honeycomb.home.management

sealed class Either<out A, out B> {
	class Left<A>(val value:A): Either<A, Nothing>()
	class Right<B>(val value: B): Either<Nothing, B>()
}

fun addEntitlement(account: String, maxUsers: Int): Either<String, Boolean> {
	return Either.Left("Bang!")
}