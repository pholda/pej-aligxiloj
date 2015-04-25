package pl.pej.malpompaaligxilo.jes2015

import pl.pej.malpompaaligxilo.form.PrintableCalculateFieldValue
import pl.pej.malpompaaligxilo.jes2015.Jes2015Kotizo.Euroj
import pl.pej.malpompaaligxilo.util.{I18n, PoCfg, Lang, I18nString}

import scala.collection.immutable.ListMap

case class Kotizo(kotizoj: ListMap[KotizoComponent, Euroj], sumo: Euroj) extends PrintableCalculateFieldValue {

  override def str(implicit lang: Lang, poCfg: PoCfg): String = {
    if (kotizoj.isEmpty) {
      I18n.t("bv. plenigi la formularon por kalkuli kotizon!")
    } else {
      (kotizoj.filterNot{case (_, prezo) => prezo == 0} map {
        case (single: SingleKotizoComponent, prezo) =>
          s"<li>${single.description(lang)}:&nbsp;%.2f€</li>".format(prezo)
        case (multimple: MultimpleKotizoComponent, prezo) =>
          s"<li>${multimple.description(lang)} (${I18n.t("%d foje %.2f€").format(multimple.pieces, multimple.singlePrice)}):&nbsp;%.2f€</li>".format(prezo)
      }).mkString("<ul>", "", "</ul>") + s"</br><strong>${I18n.t("Sume:")}</strong>&nbsp;%.2f€".format(sumo)
    }
  }
}

object Kotizo {

  def apply(kotizoj: ListMap[KotizoComponent, Euroj]): Kotizo = {
    Kotizo(kotizoj, (kotizoj.values.sum*2).ceil / 2)
  }
}
