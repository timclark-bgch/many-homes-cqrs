package connectedhome.honeycomb.handlebars

import com.github.jknack.handlebars.Handlebars
import com.github.jknack.handlebars.io.ClassPathTemplateLoader
import spark.ModelAndView
import spark.TemplateEngine

class HandlebarsTemplateEngine() : TemplateEngine() {
	private val loader = ClassPathTemplateLoader("/templates")
	private val handlebars = Handlebars(loader)

	override fun render(modelAndView: ModelAndView?): String {
		return if (modelAndView != null && modelAndView.viewName != null && modelAndView.model != null) {
			handlebars.compile(modelAndView.viewName).apply(modelAndView.model)
		} else {
			""
		}
	}
}
