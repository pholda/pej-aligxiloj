package controllers

import java.io.File
import java.net.URL

import com.google.gdata.util.ServiceForbiddenException
import pl.pej.malpompaaligxilo.form._
import pl.pej.malpompaaligxilo.form.action.AddToGoogleSpreadsheetFormAction
import pl.pej.malpompaaligxilo.googleapi.{AccountConfig, Spreadsheet}
import pl.pej.malpompaaligxilo.jes2015.Jes2015Aligxilo
import pl.pej.malpompaaligxilo.util._
import play.api.Play.current
import play.api.mvc._
import util.{MongoInsertFormAction, SendMailFormAction}
import views._

import scala.io.Source

object JES2015 extends Controller {
  implicit val context = ScalaContext

  implicit val poCfg = PoCfg.fromResources(getClass,
    "eo" -> "/jes2015_eo.po",
    "pl" -> "/jes2015_pl.po",
    "hu" -> "/jes2015_hu.po",
    "fr" -> "/jes2015_fr.po"
  )

  def index(implicit lang: Lang = "eo") = Action {
    val form = new Jes2015Aligxilo(field => Seq.empty)
    Ok(html.jes2015aligxilo(form, lang, poCfg))
  }

  def submit(implicit lang: Lang = "eo") = Action(parse.tolerantFormUrlEncoded) { implicit request =>
    val post = request.body
    implicit val form = new Jes2015Aligxilo({field =>
      val y = post.getOrElse(field.name, post.getOrElse(field.name+"[]", Nil))
      y
    }, isFilled = true)

    form.hasErrors match {
      case true =>
        Ok(html.jes2015aligxilo(form, lang, poCfg))
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
//          from = "JES-teamo <jes@pej.pl>",
          from = "Aligxilo <aligxilo@aligxilo.pholda.pl>",
          to = f => Seq(
            s"${f.personaNomo.value.get} <${f.retposxtadreso.value.get}>"
          ),
          bcc = Seq(
            "<hej.estraro@gmail.com>",
            "<tomasz.szymula@pej.pl>",
            "<piotr.holda@pej.pl>",
            "Tobiasz Kubisiowski <tobiasz.kubisiowski@pej.pl>"
          ),
          html = html.jes2015_aligxilo_mesagxo(form, lang, poCfg).body,
          headers = Seq(
            "DKIM-Signature" -> ("v=1;a=rsa;d=aligxilo.pholda.pl;s=brisbane;" +
            "c=relaxed/simple;q=dns/txt;l=1234; t=1117574938; x=1118006938;" +
            "h=from:to:subject:date;bh=MTIzNDU2Nzg5MDEyMzQ1Njc4OTAxMjM0NTY3ODkwMTI=;b=dzdVyOfAKCdLXdJOc9G2q8LoXSlEniSbav+yuU4zGeeruD00lszZVoG4ZHRNiYzR")
          )
        )

        mongoInsert.run(form)
        sendMail.run(form)

        Ok(html.jes2015_aligxilo_sukceso(form, lang, poCfg))
    }
  }
}
