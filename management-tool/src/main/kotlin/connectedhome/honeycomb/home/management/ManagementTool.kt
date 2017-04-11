package connectedhome.honeycomb.home.management

import connectedhome.honeycomb.handlebars.HandlebarsTemplateEngine
import spark.ModelAndView
import spark.Spark.*

fun main(args: Array<String>) {
	staticFiles.location("/public")
	get("/ping") { _, _ -> "PONG!" }
	get("/home") { _, _ -> render(emptyMap<String, Any>(), "overview") }
	get("/accounts") { _, _ -> render(mapOf("accounts" to results()), "accounts") }
}

private fun render(model: Map<String, Any>, template: String): String =
	HandlebarsTemplateEngine().render(ModelAndView(model, template))


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

private fun results(): List<AccountOverview> {
	return listOf(
		AccountOverview("1", "Tommy Tippee", AccountStatus.Active, listOf("E1"), listOf("P1"), listOf("U1", "U2")),
		AccountOverview("2", "Zippy Zappee", AccountStatus.Suspended, listOf("E1", "E2"), listOf("P2", "P4"), listOf("U5")),
		AccountOverview("3", "Big Bertrand", AccountStatus.Closed, listOf("E4"), listOf("P3"), listOf("U4", "U6"))
	)
}


