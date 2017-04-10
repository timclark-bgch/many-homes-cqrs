package connectedhome.honeycomb.home.management

import connectedhome.honeycomb.handlebars.HandlebarsTemplateEngine
import spark.ModelAndView
import spark.Spark.*

fun main(args: Array<String>) {
	staticFiles.location("/public")
	get("/ping") { _, _ -> "PONG!" }
	get("/home") { _, _ -> render(emptyMap<String, Any>(), "overview") }
	get("/results") { _, _ -> render(mapOf("accounts" to results()), "results") }
}

private fun render(model: Map<String, Any>, template: String): String =
	HandlebarsTemplateEngine().render(ModelAndView(model, template))


enum class AccountStatus {
	Active, Suspended, Closed
}
data class AccountOverview(val id: String, val owner: String, val status: AccountStatus, val properties: Int, val users: Int)	{
	val active = status == AccountStatus.Active
	val suspended = status == AccountStatus.Suspended
	val closed = status == AccountStatus.Closed
}

private fun results(): List<AccountOverview> {
	return listOf(
		AccountOverview("1", "Tommy Tippee", AccountStatus.Active, 1, 7),
		AccountOverview("2", "Zippy Zappee", AccountStatus.Suspended, 2, 5),
		AccountOverview("3", "Big Bertrand", AccountStatus.Closed, 1, 8)
	)
}


