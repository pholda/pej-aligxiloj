package util

import pl.pej.malpompaaligxilo.form.{Form, FormAction}
import play.api.Application
import play.api.libs.mailer.{MailerPlugin, Attachment, Email}

case class SendMailFormAction[F <: Form](
  subject: F => String,
  from: String,
  to: F => Seq[String],
  html: F => String,
  cc: F => Seq[String] = (f: F) => Nil,
  bcc: F => Seq[String] = (f: F) => Nil,
  attachments: F => Seq[Attachment] = (f: F) => Nil,
  headers: F => Seq[(String, String)] = (f :F) => Seq.empty
                               )(implicit val app: Application) extends FormAction[F] {
  override def run(form: F): Unit = {
    val email = Email(
      subject = subject(form),
      from = from,
      to = to(form),
      bodyHtml = Some(html(form)),
      charset = Some("UTF-8"),
      cc = cc(form),
      bcc = bcc(form),
      attachments = attachments(form),
      headers = headers(form)
    )

    MailerPlugin.send(email)(app)
  }
}

/*
private def sendMail(name: String, mail: String, text: String): Unit = {
    val email = Email(
      subject = s"Aliĝo al JES 2015 en Eger, Hungario - $name",
      from = "JES-teamo <jes@pej.pl>",
      to = Seq(
        s"$name <$mail>"
      ),
      bcc = Seq(
        "<hej.estraro@gmail.com>",
        "<tomasz.szymula@pej.pl>",
        "<piotr.holda@pej.pl>"
      ),
      bodyHtml = Some(text)
    )

    MailerPlugin.send(email)

    val email2 = Email(
      subject = s"Homo aliĝis al JES 2015 en Eger, Hungario - $name",
      from = "JES-teamo <jes@pej.pl>",
      to = Seq(
        "<hej.estraro@gmail.com>",
        "<holda.piotr@gmail.com>"
      ),
      bodyHtml = Some(text)
    )

    MailerPlugin.send(email2)
  }
 */