package util

import pl.pej.malpompaaligxilo.form.{Form, FormAction}
import play.api.Application
import play.api.libs.mailer.{MailerPlugin, Attachment, Email}

case class SendMailFormAction(
  subject: String,
  from: String,
  to: Form => Seq[String],
  html: Form => String,
  cc: Form => Seq[String] = _ => Nil,
  bcc: Form => Seq[String] = _ => Nil,
  attachments: Form => Seq[Attachment] = _ => Nil
                               )(implicit val app: Application) extends FormAction {
  override def run(form: Form): Unit = {
    val email = Email(
      subject = subject,
      from = from,
      to = to(form),
      bodyHtml = Some(html(form)),
      charset = Some("UTF-8"),
      cc = cc(form),
      bcc = bcc(form),
      attachments = attachments(form)
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