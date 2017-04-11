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



