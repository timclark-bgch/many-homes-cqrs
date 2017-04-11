package connectedhome.honeycomb.home.management

import connectedhome.honeycomb.handlebars.HandlebarsTemplateEngine
import spark.ModelAndView
import spark.Spark.*

fun main(args: Array<String>) {
	staticFiles.location("/public")
	get("/ping") { _, _ -> "PONG!" }
	get("/home") { _, _ -> render(emptyMap<String, Any>(), "overview") }
	get("/accounts") { _, _ -> render(mapOf("accounts" to accounts()), "accounts") }

	get("/error/:section") { req, _ -> render(mapOf("section" to "/${req.params(":section")}"), "error") }

	post("/api/account/:account/entitlements/add") { req, rsp ->
		when (addEntitlement(req.params(":account"), req.queryParams("maxUsers").toInt())) {
			is Either.Left -> rsp.redirect("/error/accounts")
			else -> rsp.redirect("/accounts")
		}

		null
	}

	post("/api/account/:account/entitlements/remove") { req, rsp ->
		if (removeEntitlement(req.params(":account"), req.queryParams("entitlement"))) {
			rsp.redirect("/accounts")
		}

		rsp.redirect("/error/accounts")
	}

	post("/api/account/:account/property/add") { req, rsp ->
		if (addProperty(req.params(":account"), req.queryParams("property"))) {
			rsp.redirect("/accounts")
		}

		rsp.redirect("/error/accounts")
	}

	post("/api/account/:account/property/remove") { req, rsp ->
		if (addProperty(req.params(":account"), req.queryParams("property"))) {
			rsp.redirect("/accounts")
		}

		rsp.redirect("/error/accounts")
	}

	post("/api/account/:account/close") { req, rsp ->
		if (close(req.params(":account"), req.queryParams("reason"))) {
			rsp.redirect("/accounts")
		}

		rsp.redirect("/error/accounts")
	}

	post("/api/account/:account/suspend") { req, rsp ->
		if (suspend(req.params(":account"), req.queryParams("reason"))) {
			rsp.redirect("/accounts")
		}

		rsp.redirect("/error/accounts")
	}

	post("/api/account/:account/activate") { req, rsp ->
		if (reactivate(req.params(":account"), req.queryParams("reason"))) {
			rsp.redirect("/accounts")
		}

		rsp.redirect("/error/accounts")
	}

}

private fun render(model: Map<String, Any>, template: String): String =
	HandlebarsTemplateEngine().render(ModelAndView(model, template))


//enum class AccountStatus {
//	Active, Suspended, Closed
//}
//
//data class AccountOverview(
//	val id: String,
//	val owner: String,
//	val status: AccountStatus,
//	val entitlements: List<String>,
//	val properties: List<String>,
//	val users: List<String>) {
//
//	val active = status == AccountStatus.Active
//	val suspended = status == AccountStatus.Suspended
//	val closed = status == AccountStatus.Closed
//
//	val entitlementCount = entitlements.size
//	val propertyCount = properties.size
//	val userCount = users.size
//}

//private fun results(): List<AccountOverview> {
//	return listOf(
//		AccountOverview("1", "Barry Gibb", AccountStatus.Active, listOf("E1"), listOf("P1"), listOf("U1", "U2")),
//		AccountOverview("2", "Robin Gibb", AccountStatus.Suspended, listOf("E1", "E2"), listOf("P2", "P4"), listOf("U5")),
//		AccountOverview("3", "Maurice Gibb", AccountStatus.Closed, listOf("E4"), listOf("P3"), listOf("U4", "U6")),
//		AccountOverview("4", "Agnetha Fältskog", AccountStatus.Closed, listOf("E4"), listOf("P3"), listOf("U4", "U6")),
//		AccountOverview("5", "Björn Ulvaeus", AccountStatus.Closed, listOf("E4"), listOf("P3"), listOf("U4", "U6")),
//		AccountOverview("6", "Benny Andersson", AccountStatus.Closed, listOf("E4"), listOf("P3"), listOf("U4", "U6")),
//		AccountOverview("7", "Anni-Frid Lyngstad", AccountStatus.Closed, listOf("E4"), listOf("P3"), listOf("U4", "U6"))
//	)
//}


