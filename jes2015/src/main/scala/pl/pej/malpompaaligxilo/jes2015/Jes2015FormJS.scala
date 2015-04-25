package pl.pej.malpompaaligxilo.jes2015

import org.scalajs.dom
import org.scalajs.dom.Element
import org.scalajs.jquery.jQuery
import pl.pej.malpompaaligxilo.form.field.CustomCalculateField
import pl.pej.malpompaaligxilo.form.{PrintableCalculateFieldValue, Field, JSContext}
import pl.pej.malpompaaligxilo.util._

import scala.collection.mutable.ListBuffer
import scala.scalajs.js
import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.JSExport

object Jes2015FormJS extends JSApp {
  implicit val context = JSContext

  implicit val lang: Lang = jQuery("html").attr("lang")
  implicit val poCfg: PoCfg = PoCfg.fromJS("formI18n")

  val form = new Jes2015Aligxilo({field =>
    field.`type`.arrayValue match {
      case false =>
        if (jQuery(s"[name=${field.name}]").is("[type=checkbox]")) {
          jQuery(s"[name=${field.name}]").is(":checked") match {
            case true => Seq("jes")
            case false => Seq.empty
          }
        } else {
          Seq(jQuery(s"[name=${field.name}]").`val`().asInstanceOf[String])
        }
      case true =>
        val checked = new ListBuffer[String]
        jQuery(s"[name='${field.name}[]']:checked").each{(a: js.Any, e: Element) =>
          checked.append(jQuery(e).`val`().asInstanceOf[String])
          a
        }
        checked.toSeq
    }
  })

  @JSExport
  def main(): Unit = {

    lazy val calculableField = form.fields.collect{
      case f@Field(_, _, CustomCalculateField(_), _, _, _, _, _, _, _) => f
    }

    def refresh() {
      form.fields.foreach{f: Field[_] =>
        try {
          val v = f.visible(form)
          jQuery(s"[name='${f.name}'], [data-name=${f.name}]").parents("div.form-group").css("display", v match {
            case true => "block"
            case false => "none"
          })
        } catch {
          case e: Exception =>
            println(s"erraro1 cxe ${f.name}, ")
        }
      }

      calculableField.foreach{
        case Field(name, _, CustomCalculateField(formula), _, _, _, _, _, _, _) =>
          try {
            jQuery(s".formExpression[data-name='$name']").html(formula(form) match {
              case Some(p: PrintableCalculateFieldValue) => p.str
              case Some(x) => x.toString
              case _ => ""
            })
          } catch {
            case e: Exception => println(s"erraro2 cxe ${e.getMessage}")
          }

      }
    }

    jQuery(":input").change{e: dom.Element =>
      refresh()
    }
    refresh()

  }
}