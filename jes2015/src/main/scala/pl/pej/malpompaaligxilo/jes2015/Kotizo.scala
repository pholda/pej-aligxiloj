package pl.pej.malpompaaligxilo.jes2015

import pl.pej.malpompaaligxilo.jes2015.Jes2015Kotizo.Euroj

import scala.collection.immutable.ListMap

case class Kotizo(kotizoj: ListMap[String, Euroj], sumo: Euroj) {
  override def toString: String = str

  def str: String = {
    if (kotizoj.isEmpty) {
      "bv. plenigi la formularon por kalkuli kotizon!"
    } else {
      (kotizoj.filterNot{case (priskribo, prezo) => prezo == 0} map {
        case (priskribo, prezo) =>
          s"<li>${priskribo}:&nbsp;%.2f€</li>".format(prezo)
      }).mkString("<ul>", "", "</ul>") + s"</br><strong>Sume:</strong>&nbsp;%.2f€".format(sumo)
    }
  }
}

object Kotizo {
  def apply(kotizoj: ListMap[String, Euroj]): Kotizo = Kotizo(kotizoj, kotizoj.values.sum)
}
