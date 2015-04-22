package pl.pej.malpompaaligxilo.jes2015

import pl.pej.malpompaaligxilo.form._
import pl.pej.malpompaaligxilo.form.errors.CustomError
import pl.pej.malpompaaligxilo.form.field._
import pl.pej.malpompaaligxilo.form.field.calculateField.CurrentDateField
import pl.pej.malpompaaligxilo.util.{Date, Dates, NoI18nString, I18nString}
import pl.pej.malpompaaligxilo.form.field.TableCheckboxField.{tuple2col, tuple2row}

import scala.collection.immutable.ListMap
import scala.util.Try

class Jes2015Aligxilo(rawFieldValue: Field[_] => Seq[String], val isFilled: Boolean = false)(implicit val context: Context) extends Form {

  override def id: String = "aligxilo"

  override protected def getRawFieldValue(field: Field[_]): Seq[String] = rawFieldValue(field)

  override def fields: List[Field[_]] =
    aligxDato ::
    personaNomo :: familiaNomo :: kromnomo :: naskigxdato :: sub18 :: genro :: retposxtadreso :: tujmesagxilo ::
      vizagxlibro ::
      telefonnumero :: lando :: adreso :: studento :: studentoFako :: invitilo :: invitiloPersonaNomo ::
      invitiloFamiliaNomo ::
      pasportnumero :: pasportoeldondato :: pasportoeldonitade :: pasportovalideco :: invitiloAdreso ::
      adresaro :: logxado :: logxadoGea :: logxadoPrefero :: logxadoKun :: cxeesto :: cxeestoElekto ::
      mangxadoBalo :: matenmangxoj :: tagmangxoj :: vespermangxoj :: matenmangxoPrefero :: mangxtipo :: mangxtipoKlarigo ::
      mangxtipo2  :: ludejoKontribuo :: ludiMuzikilon :: kielLudos :: muzikgrupoNomo :: miKunportos :: dejxoriHelpo :: gxeneralaHelpado ::
      programkontribuo :: pagado :: donacoKvoto :: donacoKialo :: donacoKialoKlarigo :: kotizo :: miPagos ::
      komento :: regularo :: Nil

  val aligxDato = Field(
    name = "aligxDato",
    caption = NoI18nString("aligxDato"),
    visible = false,
    store = true,
    `type` = CurrentDateField()
  )

  val personaNomo = Field(
    name = "personaNomo",
    caption = I18nString(
      "eo" -> "Persona Nomo",
      "pl" -> "Imie"
    ),
    description = Some(I18nString(
      "eo" -> "por identigilo kaj adresaro"
    )),
    required = true,
    `type` = StringField()
  )
  val familiaNomo = Field(
    name = "familiaNomo",
    caption = I18nString(
      "eo" -> "Familia Nomo",
      "pl" -> "Imie"
    ),
    description = Some(I18nString(
      "eo" -> "por identigilo kaj adresaro"
    )),
    required = true,
    `type` = StringField()
  )
  val kromnomo = Field(
    name = "kromnomo",
    caption = I18nString(
      "eo" -> "Kromnomo",
      "pl" -> "Pseudonim"
    ),
    description = Some(I18nString(
      "eo" -> "Kion vi ŝatus aperigi sur via ŝildo?"
    )),
    `type` = StringField()
  )
  val naskigxdato = Field(
    name = "naskigxdato",
    caption = I18nString(
      "eo" -> "Naskiĝdato",
      "pl" -> "Data urodzenia"
    ),
    required = true,
    `type` = DateField(
      minDate = Some("1900-01-01"),
      maxDate = Some("2014-12-31"),
      yearRange = Some("1900:2014")
    )
  )
  val sub18 = Field(
    name = "sub18",
    caption = NoI18nString(""),
    visible = {implicit form =>
      naskigxdato.value match {
        case Some(date: Date) =>
          Try{
            val unuantagon18jaroj = form.dates.fromString("1997-12-27")
            form.dates.fromString("1997-12-27") < date
          }.getOrElse(false)
        case _ =>
          false
      }
    },
    store = false,
    `type` = CustomCalculateField[String]{form =>
      Some("Vi estos sub 18 dum la JES, vi bezonas sendi skanitan gepatran permesilon al la organizantoj pri via partopreno, se vi venos sole al la renkontiĝo.")
    }
  )
  val genro = Field(
    name = "genro",
    caption = I18nString(
      "eo" -> "Genro",
      "pl" -> "Płeć"
    ),
    `type` = SelectField(
      options = List(
        EnumOption(
          value = "ina",
          caption = I18nString(
            "eo" -> "Ina",
            "pl" -> "Żeńska"
          )
        ),
        EnumOption(
          value = "malina",
          caption = I18nString(
            "eo" -> "Malina",
            "pl" -> "Męska"
          )
        ),
        EnumOption(
          value = "alia",
          caption = I18nString(
            "eo" -> "Alia",
            "pl" -> "Inna"
          )
        )
      )
    )
  )
  val retposxtadreso = Field(
    name = "retposxtadreso",
    caption = I18nString(
      "eo" -> "Retpoŝtadreso",
      "pl" -> "Adres email"
    ),
    required = true,
    `type` = EmailField
  )
  val tujmesagxilo = Field(
    name = "tujmesagxilo",
    caption = I18nString(
      "eo" -> "Tujmesaĝilo"
    ),
    description = Some(I18nString("eo" -> "ekz. Skype: …")),
    `type` = StringField()
  )
  val vizagxlibro = Field(
    name = "vizagxlibro",
    caption = I18nString(
      "eo" -> "FB-adreso"
    ),
    `type` = StringField(
      default = Some("https://www.facebook.com/")
    )
  )
  val telefonnumero = Field(
    name = "telefonnumero",
    caption = I18nString(
      "eo" -> "Telefonnumero"
    ),
    placeholder = Some(I18nString("eo" -> "internacia formo (ekz. +36 …)")),
    description = Some(I18nString("eo" -> "Ni uzos ĝin nur por organizaj celoj (ekz. por retrovi vin, se vi perdiĝus).")),
    `type` = StringField()
  )
  val lando = countryField
  val adreso = Field(
    name = "adreso",
    caption = I18nString("eo" -> "Adreso"),
    description = Some(I18nString("eo" -> "Por adresaro.")),
    `type` = StringField(textarea = true)
  )
  val studento = Field(
    name = "studento",
    caption = I18nString("eo" -> "Mi estas plentempa studento/lernanto"),
    `type` = CheckboxField(default = false)
  )
  val studentoFako = Field(
    name = "studentoFako",
    caption = I18nString("eo" -> "Mi studas/lernas"),
    `type` = StringField(),
    visible = {implicit form =>
      studento.value == Some(true)
    }
  )


  val invitilo = Field(
    name = "invitilo",
    caption = I18nString("eo" -> "Mi bezonas invitilon"),
    description = Some(I18nString("eo" -> "Se vi bezonas invitilon, indiku ĉi tie plej malfrue ĝis la 1a de septembro. Se oni post tio ne kontaktus vin ene de unu semajno, bv. skribi al la respondeculo (Saci).")),
    `type` = SelectField(
      options = List(
        EnumOption("ne", I18nString("eo" -> "ne")),
        EnumOption("jes", I18nString("eo" -> "jes"))
      )
    )
  )

  val invitiloPersonaNomo = Field(
    name = "invitiloPersonaNomo",
    caption = I18nString(
      "eo" -> "Persona Nomo",
      "pl" -> "Imie"
    ),
    description = Some(I18nString(
      "eo" -> "por invitilo"
    )),
    visible = {implicit form =>
      invitilo.value.exists(_.value == "jes")
    },
    `type` = StringField()
  )
  val invitiloFamiliaNomo = Field(
    name = "invitiloFamiliaNomo",
    caption = I18nString(
      "eo" -> "Familia Nomo",
      "pl" -> "Imie"
    ),
    description = Some(I18nString(
      "eo" -> "por invitilo"
    )),
    visible = {implicit form =>
      invitilo.value.exists(_.value == "jes")
    },
    `type` = StringField()
  )
  val pasportnumero = Field(
    name = "pasportnumero",
    caption = I18nString("eo" -> "Numero de pasporto"),
    visible = {implicit form =>
      invitilo.value.exists(_.value == "jes")
    },
    `type` = StringField()
  )
  val pasportoeldondato = Field(
    name = "pasportoeldondato",
    caption = I18nString("eo" -> "Eldondato de pasporto"),
    visible = {implicit form =>
      invitilo.value.exists(_.value == "jes")
    },
    `type` = DateField(yearRange = Some("2005:2015"))
  )
  val pasportoeldonitade = Field(
    name = "pasportoeldonitade",
    caption = I18nString("eo" -> "Pasporto eldonita de"),
    visible = {implicit form =>
      invitilo.value.exists(_.value == "jes")
    },
    `type` = StringField()
  )
  val pasportovalideco = Field(
    name = "pasportovalideco",
    caption = I18nString("eo" -> "Valideco de pasporto"),
    visible = {implicit form =>
      invitilo.value.exists(_.value == "jes")
    },
    `type` = DateField(yearRange = Some("2015:2035"))
  )
  val invitiloAdreso = Field(
    name = "invitiloAdreso",
    caption = I18nString("eo" -> "Adreso"),
    description = Some(I18nString("eo" -> "Adreso por sendi la invitilon.")),
    visible = {implicit form =>
      invitilo.value.exists(_.value == "jes")
    },
    `type` = StringField(textarea = true)
  )
  
  
  
  val adresaro = Field(
    name = "adresaro",
    caption = I18nString("eo" -> "Adresaro"),
    description = Some(I18nString("eo" -> "Post la aranĝo ni sendas al la partoprenantoj adresaron. Ĉu vi ŝatus aperi en ĝi? Se jes, kun kiuj datumoj?")),
    `type` = TableCheckboxField(
      cols = List("jes" -> NoI18nString("jes")),
      rows = List(
        "personaNomo" -> I18nString("eo" -> "Persona Nomo"),
        "familiaNomo" -> I18nString("eo" -> "Familia Nomo"),
        "kromnomo" -> I18nString("eo" -> "Kromnomo"),
        "retposxtadreso" -> I18nString("eo" -> "Retpoŝtadreso"),
        "adreso" -> I18nString("eo" -> "Adreso"),
        "tujmesagxilo" -> I18nString("eo" -> "Tujmesaĝilo")
      ),
      default = true
    )
  )
  val logxado = Field(
    name = "logxado",
    caption = I18nString(
      "eo" -> "Loĝado"
    ),
    //2-lita, 4-5 lita, amasloĝejo sur matraco, amasloĝejo surplanke
    `type` = SelectField(
      options = List(
        EnumOption("2-lita-cxambro", I18nString("eo" -> "2/3 lita kun duŝejo")),
        EnumOption("kundusxeja", I18nString("eo" -> "4/5/6 lita kun duŝejo")),
        EnumOption("4-lita-cxambro-sen-dusxejo", I18nString("eo" -> "4 lita sen duŝejo")),
//        EnumOption("14-lita-cxambro-sen-dusxejo", I18nString("eo" -> "14 lita kun duŝejo en apuda konstruaĵo")),
        EnumOption("amaslogxejo-matraco", I18nString("eo" -> "amasloĝejo sur matraco")),
        EnumOption("amaslogxejo-surplanke", I18nString("eo" -> "amasloĝejo surplanke")),
        EnumOption("memzorganto", I18nString("eo" -> "memzorganto"))
      )
    )
  )
  val logxadoGea = Field(
    name = "logxado-gea",
    caption = I18nString(
      "eo" -> "Ĉu via ĉambro rajtas esti gea?"
    ),
    visible = {implicit form =>
      logxado.value.forall(_.value != "memzorganto")
    },
    `type` = SelectField(
      options = List(
        EnumOption("ne", I18nString("eo" -> "ne")),
        EnumOption("jes", I18nString("eo" -> "jes"))
      )
    )
  )
  val logxadoPrefero = Field(
    name = "logxadoPrefero",
    caption = I18nString(
      "eo" -> "Mi preferas loĝi en … ĉambro."
    ),
    visible = {implicit form =>
      logxado.value.forall(_.value != "memzorganto")
    },
    `type` = SelectField(
      options = List(
        EnumOption("egalas", I18nString("eo" -> "egalas")),
        EnumOption("trankvila", I18nString("eo" -> "trankvila")),
        EnumOption("dibocxa", I18nString("eo" -> "diboĉa"))
      )
    )
  )
  val logxadoKun = Field(
    name = "logxadoKun",
    caption = I18nString(
      "eo" -> "Mi preferus loĝi kun…"
    ),
    visible = {implicit form =>
      logxado.value.forall(_.value != "memzorganto")
    },
    `type` = StringField()
  )
  val cxeesto = Field(
    name = "cxeesto",
    caption = I18nString("eo" -> "Ĉeesto"),
    `type` = SelectField(List(
      EnumOption("cxiun", I18nString("eo" -> "ĉiun tagon")),
      EnumOption("elekto", I18nString("eo" -> "partatempe")),
      EnumOption("balo", I18nString("eo" -> "nur silvestra balo"))
    ))
  )
  val cxeestoElekto = Field(
    name = "cxeestoElekto",
    caption = I18nString("eo" -> ""),
    description = Some(I18nString("eo" -> "Bv. indiki la TRANOKTOJN kiam vi partoprenos.")),
    visible = {implicit form =>
      cxeesto.value.exists(_.value == "elekto")
    },
    `type` = TableCheckboxField(
      cols = List("jes" -> NoI18nString("jes")),
      rows = List(
        "27/28" -> NoI18nString("27/28"),
        "28/29" -> NoI18nString("28/29"),
        "29/30" -> NoI18nString("29/30"),
        "30/31" -> NoI18nString("30/31"),
        "31/1" -> NoI18nString("31/1"),
        "1/2" -> NoI18nString("1/2"),
        "2/3" -> NoI18nString("2/3")
      )
    )
  )

  val matenmangxoj = Field(
    name = "matenmangxoj",
    caption = I18nString(
      "eo" -> "Matenmanĝoj"
    ),
    description = Some(I18nString(
      "eo" -> "Se vi elektas tion vi matenmanĝos post ĉiu via tranokto."
    )),
    `type` = CheckboxField(default = true)
  )
  val tagmangxoj = Field(
    name = "tagmangxoj",
    caption = I18nString(
      "eo" -> "Tagmanĝoj"
    ),
    description = Some(I18nString(
      "eo" -> "Se vi elektas tion vi matenmanĝos inter ĉiu via tranokto."
    )),
    `type` = CheckboxField(default = true)
  )
  val vespermangxoj = Field(
    name = "vespermangxoj",
    caption = I18nString(
      "eo" -> "Vespermanĝoj"
    ),
    description = Some(I18nString(
      "eo" -> "Se vi elektas tion vi vespermanĝos antaŭ ĉiu via tranokto."
    )),
    `type` = CheckboxField(default = true)
  )

  val mangxadoBalo = Field(
    name = "mangxadoBalo",
    caption = I18nString(
      "eo" -> "Manĝtipo por la silvestra balo"
    ),
    description = Some(I18nString("eo" -> "Eĉ se vi estas memzorganto, ni bezonas scii vian kutiman manĝtipon por la silvestra balo.")),
    visible = {implicit form =>
      (matenmangxoj.value != Some(true) &&
        vespermangxoj.value != Some(true) &&
        tagmangxoj.value != Some(true)) && {
        def x = cxeestoElekto.value.getOrElse(Set.empty)

        cxeesto.value.exists(_.value == "cxiun") ||
          x.exists(_._1.id == "31/1")
      }
    },
    `type` = StringField()
  )
  val matenmangxoPrefero = Field(
    name = "matenmangxoPrefero",
    caption = I18nString(
      "eo" -> "Mi preferas matenmanĝi…"
    ),
    visible = { implicit form =>
      matenmangxoj.value == Some(true)
    },
    `type` = SelectField(
      options = List(
        EnumOption("ne_gravas", I18nString("eo" -> "ne gravas")),
        EnumOption("dolcxe", I18nString("eo" -> "dolĉe")),
        EnumOption("sale", I18nString("eo" -> "sale"))
      )
    )
  )
  val mangxtipo = Field(
    name = "mangxtipo",
    caption = I18nString("eo" -> "Mi volas manĝi…"),
    visible = {implicit form =>
      matenmangxoj.value == Some(true) ||
      vespermangxoj.value == Some(true) ||
      tagmangxoj.value == Some(true)
    },
    `type` = SelectField(
      options = List(
        EnumOption("viande", I18nString("eo" -> "viande")),
        EnumOption("vegetare", I18nString("eo" -> "vegetare")),
        EnumOption("vegane", I18nString("eo" -> "vegane")),
        EnumOption("speciale", I18nString("eo" -> "alimeniere/speciale"))
      )
    )
  )
  val mangxtipoKlarigo = Field(
    name = "mangxtipoKlarigo",
    caption = NoI18nString(""),
    description = Some(I18nString("eo" -> "Baze temas pri alergio aŭ trosentemo, ekz. kontraŭ gluteno k.s. Bv. do ĉi tie vortigi, kial kaj kian specialan manĝon vi bezonas.")),
    visible = {implicit form: Form =>
      ( matenmangxoj.value == Some(true) ||
        vespermangxoj.value == Some(true) ||
        tagmangxoj.value == Some(true) ) && mangxtipo.value.exists(_.value == "speciale")
    },
    `type` = StringField()
  )
  val mangxtipo2 = Field(
    name = "mangxtipo2",
    caption = I18nString("eo" -> "Mi volas havi…"),
    description = Some(I18nString("eo" -> "Ni planas la menuon laŭ la respondoj, tio estas nur por informiĝi, vi ne nepre ricevos laŭ via elekto")),
    visible = {implicit form =>
      mangxtipo.value.exists(_.value == "viande") &&
        (
          matenmangxoj.value == Some(true) ||
            vespermangxoj.value == Some(true) ||
            tagmangxoj.value == Some(true)
          )
    },
    `type` = SelectField(
      options = List(
        EnumOption("viandega", I18nString("eo" -> "viandon kun viando! (mi volas havi viandaĵon por ĉiu plado)")),
        EnumOption("nenurvianda", I18nString("eo" -> "viandon kun aliaj nutraĵoj! (ĉiutage viandon, sed ne nur viandajn pladojn)"))
      )
    )
  )
  val ludejoKontribuo = Field(
    name = "ludejoKontribuo",
    caption = I18nString("eo" -> "Mi kontribuos al la ludejo per"),
    `type` = TableCheckboxField(
      rows = List(
        TableCheckboxRow(
          "kunporti-ludilojn",
          I18nString("eo" -> "Mi pretas kunporti mia(j)n plej ŝatata(j)n ludilojn por la ludejo.")
        ),
        TableCheckboxRow(
          "lud-sesio",
          I18nString("eo" -> "Mi pretas gvidi ludsesiojn en la ludejo.")
        )
      ),
      cols = List(TableCheckboxCol("jes", NoI18nString("jes")))
    ),
    separateValues = { f: Option[Set[(TableCheckboxRow, TableCheckboxCol)]] =>
      f match {
        case Some(values) =>
          Some(List("ludejoKontribuo" -> values.map(_._1.id).mkString(", ")))
        case _ => None
      }
    }
  )
  val ludiMuzikilon = Field(
    name = "ludiMuzikilon",
    caption = I18nString("eo" -> "Ĉu vi kantas, aŭ ludas iun muzikilon, kiun vi volonte kunportus?"),
    `type` = SelectField(List(
      EnumOption("jes", I18nString("eo" -> "jes")),
      EnumOption("ne", I18nString("eo" -> "ne"))
    ))
  )
  val kielLudos = Field(
    name = "kielLudos",
    caption = I18nString("eo" -> "Kiel vi volonte uzus vian kantkapablon / muzikilon?"),
    visible = {implicit form =>
      ludiMuzikilon.value.exists(_.value == "jes")
    },
    `type` = TableCheckboxField(
      cols = List("jes" -> NoI18nString("jes")),
      rows = List(
        "mi-ludus" -> I18nString("eo" -> "Mi ne certas, sed mi volonte ludus aŭ sole, aŭ kune kun kelkaj aliaj dum la semajno"),
        "mi-prelegus" -> I18nString("eo" -> "Mi pretas prelegi pri kantado / muzikiloj / muziko / io ajn"),
        "mi-gvidus-kurson" -> I18nString("eo" -> "Mi pretas gvidi kurson pri kantado / muzikilludado"),
        "mi-koncertus-gufuje" -> I18nString("eo" -> "Mi pretas fari 20-30 minutan gufujan koncerteton"),
        "mi-koncertus-vespere" -> I18nString("eo" -> "Mi pretas aŭ sole, aŭ kun aliaj (propra muzikgrupo, spontanea ludado ktp.) fari 45-60 minutan koncerton, kiel vespera programo")
      )
    ),
    separateValues = { f: Option[Set[(TableCheckboxRow, TableCheckboxCol)]] =>
      f match {
        case Some(values) =>
          Some(List("kielLudos" -> values.map(_._1.id).mkString(", ")))
        case _ => None
      }
    }
  )
  val muzikgrupoNomo = Field(
    name = "muzikgrupoNomo",
    caption = I18nString("eo" -> "Detaloj de artisto/muzikgrupo"),
    description = Some(I18nString("eo" -> "Nomo, kiom longe la koncerto daŭros, kiajn teĥnikaĵojn vi bezonas ktp.")),
    `type` = StringField(textarea = true),
    visible = {implicit form =>
      kielLudos.value.exists(_.exists{case (r,c) => c.id == "mi-koncertus-vespere"})
    }
  )
  val miKunportos = Field(
    name = "miKunportos",
    caption = I18nString("eo" -> "Kiun muzikilon vi kunportos"),
    visible = {implicit form =>
      ludiMuzikilon.value.exists(_.value == "jes")
    },
    `type` = TableCheckboxField(
      cols = List("jes" -> NoI18nString("jes")),
      rows = List(
        "gitaro" -> I18nString("eo" -> "Gitaron"),
        "violono" -> I18nString("eo" -> "Violonon"),
        "transversaFluto" -> I18nString("eo" -> "Transversan fluton"),
        "bekfluto" -> I18nString("eo" -> "Bekfluton"),
        "ukulelo" -> I18nString("eo" -> "Ukulelon"),
        "alia" -> I18nString("eo" -> "Alian muzikilon"),
        "ne-scias" -> I18nString("eo" -> "Mi ankoraŭ ne decidis, sed mi certe kunportos ion")
      )
    ),
    separateValues = {f: Option[Set[(TableCheckboxRow, TableCheckboxCol)]] =>
      f match {
        case Some(values) =>
          Some(List("miKunportos" -> values.map(_._1.id).mkString(", ")))
        case _ => None
      }
    }
  )
  val dejxoriHelpo = Field(
    name = "dejxoriHelpo",
    caption = I18nString("eo" -> "Mi proponas"),
    `type` = TableCheckboxField(
      rows = List(
        TableCheckboxRow(
          "dejxori-gufujo",
          I18nString("eo" -> "deĵori en la gufujo")
        ),
        TableCheckboxRow(
          "dejxori-trinkejo",
          I18nString("eo" -> "deĵori en la trinkejo")
        )
      ),
      cols = List(TableCheckboxCol("jes", NoI18nString("jes")))
    )
  )
  val gxeneralaHelpado = Field(
    name = "gxeneralaHelpado",
    caption = I18nString("eo" -> "Ĝenarala helpado"),
    description = Some(I18nString("eo" -> "Se vi volonte helpos al ni pri ĝeneralaj taskoj bv. ĉi tie indiki (ekzemple per pakado de tabloj en la ejo, gvidado de la grupo al la ekstera halo).")),
    `type` = CheckboxField()
  )
  val programkontribuo = Field(
    name = "programkontribuo",
    caption = I18nString("eo" -> "Programkontribuo"),
    description = Some(I18nString("eo" -> "Se vi ŝatus fari programeron, bv. skribu ĉi tien mallonge vian proponon! Vi ricevos retleteron de la organizantoj responde al via propono.")),
    `type` = StringField(textarea = true)
  )
  val pagado = Field(
    name = "pagado",
    caption = I18nString("eo" -> "Pagado"),
    required = true,
    `type` = SelectField(List(
      EnumOption("uea", I18nString("eo" -> "UEA-konto (jesm-f)")),
      EnumOption("hungara", I18nString("eo" -> "la hungara konto")),
      EnumOption("pola", I18nString("eo" -> "la pola konto"))
    ))
  )
  val donacoKvoto = Field(
    name = "donacoKvoto",
    caption = I18nString("eo" -> "Mi ŝatus donaci al JES 2015…"),
    placeholder = Some(I18nString("eo" -> "entajpu sumon (en eŭroj)")),
    description = Some(I18nString("eo" -> "Se vi ŝatus subteni la eventon per iom da mono, ĝi certe bonvenas. :) Vi eĉ povas indiki, por kia celo ni elspezu ĝin!")),
    `type` = IntField(min = Some(0))
  )
  val donacoKialo = Field(
    name = "donacoKialo",
    caption = I18nString("eo" -> "Mi ŝatus donaci por"),
    `type` = SelectField(List(
      EnumOption("subteni-partoprenantojn", I18nString("eo" -> "subteni malriĉajn junajn partoprenantojn")),
      EnumOption("ricxa-programo", I18nString("eo" -> "pli riĉigan programon")),
      EnumOption("alia", I18nString("eo" -> "alia"))
    ))
  )
  val donacoKialoKlarigo = Field(
    name = "donacoKialoKlarigo",
    caption = NoI18nString(""),
    description = Some(I18nString("eo" -> "Bv klarigi.")),
    visible = {implicit form =>
      donacoKialo.value.exists(_.value == "alia")
    },
    `type` = StringField()
  )
  val kotizo = Field(
    name = "kotizo",
    caption = I18nString("eo" -> "Kalkulita kotizo"),
    description = Some(I18nString("eo" -> "En eŭroj")),
    `type` = CustomCalculateField[Kotizo]{f =>
      try {
        Some(Jes2015Kotizo.kotizo(f.asInstanceOf[Jes2015Aligxilo]))
      } catch {
        case e => None
      }
    },
    separateValues = {k: Option[Kotizo] => {
      k match {
        case Some(kotizo) => Some(List("kotizo" -> "%1.00f".format(kotizo.sumo)))
        case None => None
      }
    }}
  )
  val miPagos = Field(
    name = "miPagos",
    caption = I18nString("eo" -> "Ene de la nuna aliĝperiodo mi pagos.."),
    description = Some(I18nString("eo" -> "Se vi pagos la tutan kotizon ĝis la 31-a de aŭgusto, vi ricevos rabaton je 5€")),
    required = true,
    `type` = SelectField(List(
      EnumOption("tuton", I18nString("eo" -> "tutan sumon por helpi en organizado kaj ĝui 5-eŭran rabaton")),
      EnumOption("antaupagon", I18nString("eo" -> "nur necesan antaŭpagon (40 eŭr)"))
    ))
  )

  /*
   tio probable ne necesos
  val miPagosGxis = Field(
    name = "miPagosGxis",
    caption = I18nString("eo" -> "Mi pagos ĝis…"),
    description = Some(I18nString("eo" -> "* - studentoj povas pagi duonon da sumo poste, la dua dato tion indikas.")),
    required = true,
    `type` = SelectField(List(
      EnumOption("rabato30", I18nString("eo" -> "antaŭpago ĝis 30.2, pago* ĝis 30.04 (30% rabato)")),
      EnumOption("rabato24", I18nString("eo" -> "antaŭpago ĝis 30.4, pago* ĝis 30.07 (24% rabato)")),
      EnumOption("rabato18", I18nString("eo" -> "antaŭpago ĝis 30.8, pago* ĝis 30.08 (18% rabato)")),
      EnumOption("rabato12", I18nString("eo" -> "antaŭpago ĝis 30.11, pago* ĝis 30.11 (12% rabato)")),
      EnumOption("rabato0", I18nString("eo" -> "decembre"))
    )),
    visible = {implicit form =>
      !invitilo.value.exists(_.value == "jes")
    }
  )
  val miPagosGxisAlt = Field(
    name = "miPagosGxisAlt",
    caption = I18nString("eo" -> "Recivinte vizon mi…"),
    description = Some(I18nString("eo" -> "* - studentoj povas pagi duonon da sumo poste, la dua dato tion indikas.")),
    required = true,
    `type` = SelectField(List(
      EnumOption("antaupago", I18nString("eo" -> "ŭuj antaŭpagos 20 eŭrojn, la reston decembre")),
      EnumOption("tuton", I18nString("eo" -> "pagos la tuton kaj ricevos rabaton"))
    )),
    visible = {implicit form =>
      invitilo.value.exists(_.value == "jes")
    }
  )*/
  val komento = Field(
    name = "komento",
    caption = I18nString("eo" -> "Komento"),
    description = Some(I18nString("eo" -> "Ĉi tie vi povas lasi mesaĝon al la organizantoj aŭ aldoni komentojn al via aliĝo! Dankon!")),
    `type` = StringField(textarea = true)
  )
  val regularo = Field(
    name = "regularo",
    caption = I18nString("eo" -> "Mi akceptas la regularon de la aranĝo"),
    required = true,
    `type` = CheckboxField(),
    customValidate = {v: Boolean =>
      if (!v)
        Some(new CustomError(I18nString("eo" -> "vi devas akcepti la regularon!")))
      else
        None
    }
  )
}
