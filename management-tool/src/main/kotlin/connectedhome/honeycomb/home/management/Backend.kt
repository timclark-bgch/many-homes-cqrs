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

enum class AccountStatus {
	Active, Suspended, Closed
}

data class AccountOverview(
	val id: String,
	val owner: String,
	val status: AccountStatus,
	val entitlements: List<String>,
	val properties: List<String>,
	val users: List<String>) {

	val active = status == AccountStatus.Active
	val suspended = status == AccountStatus.Suspended
	val closed = status == AccountStatus.Closed

	val entitlementCount = entitlements.size
	val propertyCount = properties.size
	val userCount = users.size
}

fun accounts(): List<AccountOverview> =
	listOf(
		AccountOverview("1", "Barry Gibb", AccountStatus.Active, listOf("E1"), listOf("P1"), listOf("U1", "U2")),
		AccountOverview("2", "Robin Gibb", AccountStatus.Suspended, listOf("E1", "E2"), listOf("P2", "P4"), listOf("U5")),
		AccountOverview("3", "Maurice Gibb", AccountStatus.Closed, listOf("E4"), listOf("P3"), listOf("U4", "U6")),
		AccountOverview("4", "Agnetha Fältskog", AccountStatus.Closed, listOf("E4"), listOf("P3"), listOf("U4", "U6")),
		AccountOverview("5", "Björn Ulvaeus", AccountStatus.Closed, listOf("E4"), listOf("P3"), listOf("U4", "U6")),
		AccountOverview("6", "Benny Andersson", AccountStatus.Closed, listOf("E4"), listOf("P3"), listOf("U4", "U6")),
		AccountOverview("7", "Anni-Frid Lyngstad", AccountStatus.Closed, listOf("E4"), listOf("P3"), listOf("U4", "U6"))
	)
