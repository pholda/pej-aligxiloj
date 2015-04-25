package pl.pej.malpompaaligxilo.jes2015

import pl.pej.malpompaaligxilo.jes2015.Jes2015Kotizo.Euroj
import pl.pej.malpompaaligxilo.util.I18nableString

sealed abstract class KotizoComponent

case class SingleKotizoComponent(description: I18nableString) extends KotizoComponent

case class MultimpleKotizoComponent(description: I18nableString, singlePrice: Euroj, pieces: Int) extends KotizoComponent
