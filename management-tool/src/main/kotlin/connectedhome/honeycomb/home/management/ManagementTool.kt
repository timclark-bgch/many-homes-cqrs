package connectedhome.honeycomb.home.management

import connectedhome.honeycomb.handlebars.HandlebarsTemplateEngine
import spark.ModelAndView
import spark.Spark.*

fun main(args: Array<String>) {
	staticFiles.location("/public")
	get("/ping") { req, rsp -> "PONG!" }
	get("/home") { _, _ -> render(emptyMap<String, Any>(), "overview") }
	get("/results") { _, _ -> render(emptyMap<String, Any>(), "results") }
}

private fun render(model: Map<String, Any>, template: String): String =
	HandlebarsTemplateEngine().render(ModelAndView(model, template))


