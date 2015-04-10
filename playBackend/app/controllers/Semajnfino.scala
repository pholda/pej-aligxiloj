package controllers

import java.io.File
import java.net.URL

import com.google.gdata.util.ServiceForbiddenException
import controllers.JES2015._
import pl.pej.malpompaaligxilo.form.ScalaContext
import pl.pej.malpompaaligxilo.form.action.AddToGoogleSpreadsheetFormAction
import pl.pej.malpompaaligxilo.googleapi.{AccountConfig, Spreadsheet}
import pl.pej.malpompaaligxilo.semajnfino.SemajnfinoAligxilo
import play.api.mvc.Action
import util.SendMailFormAction
import views.html
import play.api.Play.current

object Semajnfino {
  implicit val context = ScalaContext

  def index = Action {
    val form = SemajnfinoAligxilo(isFilled = false, field => Seq.empty)
    Ok(views.html.semajnfino.form(form))
  }

  def submit = Action(parse.tolerantFormUrlEncoded) { implicit request =>
    val post = request.body
    implicit val form = SemajnfinoAligxilo(isFilled = true, {field =>
      post.getOrElse(field, post.getOrElse(field+"[]", Nil))
    })

    form.hasErrors match {
      case true =>
        Ok(views.html.semajnfino.form(form))
      case false =>
        implicit val accountConfig = AccountConfig(
          serviceAccountId = "90211112486-s84fu8sqfg5hlnjcgglucup6nap73qnh@developer.gserviceaccount.com",
          p12PrivateKey = new File("MalmpompaAligxilo-0f665c366cc1.p12")
        )
        val spreadsheet = Spreadsheet(new URL(
          "https://spreadsheets.google.com/feeds/worksheets/1DdHSMIHyviwx03UNug-isaltFqty-hY9UwbUnGdpHN4/private/full"
        ))
        val gaction = AddToGoogleSpreadsheetFormAction(
          spreadsheet, "aligxilo"
        )
        try {
          gaction.run(form)
        } catch {
          case _:ServiceForbiddenException => throw new Exception("google forbidden")
        }

        val sendMailFormAction = SendMailFormAction(
          subject = "PEJ semajnfino",
          from = "Pola Esperanto-Junularo <pej@pej.pl>",
          to = Seq(form.personaNomo.value.get +" <" + form.retposxtadreso.value.get +" >"),
          bcc = Seq("Piotr Hołda <piotr.holda@pej.pl>"),
          html = views.html.semajnfino.mail(form).body
        )
//        try {
          sendMailFormAction.run(form)
//        } catch {
//          case _ =>
//        }
//        val sendMail = SendMailFormAction(SV300S37A
//           "pej semajnfino")

        Ok(views.html.semajnfino.success(form))
        //        val msg = html.jes2015_aligxilo_mesagxo(form)
        //        sendMail( s"${parsed.fields(form.personaNomo).value.get} ${parsed.fields(form.familiaNomo).value.get}",
        //          parsed.fields(form.retposxtadreso).value.get.toString, msg.body)
        //        mongoInsert(parsed.data.map{field => field.name -> field.value}.toMap, form)
//        Ok(html.jes2015_aligxilo_sukceso(form))
    }
  }
}