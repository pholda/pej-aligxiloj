package controllers

import java.io.File
import java.net.URL
import java.text.DecimalFormat

import com.google.gdata.util.ServiceForbiddenException
import com.mongodb.casbah.MongoClient
import com.mongodb.casbah.commons.MongoDBObject
import org.joda.time.DateTime
import pl.pej.malpompaaligxilo.form._
import pl.pej.malpompaaligxilo.form.action.AddToGoogleSpreadsheetFormAction
import pl.pej.malpompaaligxilo.form.field._
import pl.pej.malpompaaligxilo.googleapi.{Spreadsheet, AccountConfig}
import pl.pej.malpompaaligxilo.jes2015.Jes2015Aligxilo
import pl.pej.malpompaaligxilo.util.DatesScala
import play.api.Play.current
import play.api.libs.mailer.{Email, MailerPlugin}
import play.api.mvc._
import util.{SendMailFormAction, MongoInsertFormAction}
import views._

object JES2015 extends Controller {
  implicit val context = ScalaContext
  
  def index = Action {
    val form = new Jes2015Aligxilo(field => Seq.empty)
    Ok(html.jes2015aligxilo(form))
  }

  def submit = Action(parse.tolerantFormUrlEncoded) { implicit request =>
    val post = request.body
    implicit val form = new Jes2015Aligxilo({field =>
      val y = post.getOrElse(field.name, post.getOrElse(field.name+"[]", Nil))
      y
    }, isFilled = true)

    form.hasErrors match {
      case true =>
        Ok(html.jes2015aligxilo(form))
      case false =>

        implicit val accountConfig = AccountConfig(
          serviceAccountId = "90211112486-s84fu8sqfg5hlnjcgglucup6nap73qnh@developer.gserviceaccount.com",
          p12PrivateKey = new File("MalmpompaAligxilo-0f665c366cc1.p12")
        )
        val spreadsheet = Spreadsheet(new URL(
          "https://spreadsheets.google.com/feeds/worksheets/1inSQY_2E5aQk9hKUMEKjzW71QBiEllRP_-UuOVlcZeo/private/full"
        ))
        val gaction = AddToGoogleSpreadsheetFormAction(
          spreadsheet, "aligxintoj"
        )
        try {
          gaction.run(form)
        } catch {
          case _:ServiceForbiddenException => throw new Exception("google forbidden")
        }

        val mongoInsert = MongoInsertFormAction("jes2015", "aligxintoj")

        val sendMail = SendMailFormAction[Jes2015Aligxilo](
          subject = f => s"Aliĝo al JES 2015 en Eger, Hungario - ${f.personaNomo.value.get}",
          from = "JES-teamo <jes@pej.pl>",
          to = f => Seq(
            s"${f.personaNomo.value.get} <${f.retposxtadreso.value.get}>"
          ),
          bcc = Seq(
            "<hej.estraro@gmail.com>",
            "<tomasz.szymula@pej.pl>",
            "<piotr.holda@pej.pl>"
          ),
          html = html.jes2015_aligxilo_mesagxo(form, "eo").body
        )

        mongoInsert.run(form)
        sendMail.run(form)

        Ok(html.jes2015_aligxilo_sukceso(form))
    }
  }
}
