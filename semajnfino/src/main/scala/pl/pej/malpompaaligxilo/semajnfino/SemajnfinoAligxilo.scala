package pl.pej.malpompaaligxilo.semajnfino

import pl.pej.malpompaaligxilo.form._
import pl.pej.malpompaaligxilo.form.field.calculateField.CurrentDateField
import pl.pej.malpompaaligxilo.form.field.{EnumOption, SelectField, EmailField, StringField}
import pl.pej.malpompaaligxilo.util.{NoI18nString, Dates, I18nString}

case class SemajnfinoAligxilo(isFilled: Boolean, _getRawFieldValue: FieldName => Seq[String])(implicit val context: Context) extends Form {
  override def id: String = "semajnfinoAligxilo"

  override protected def getRawFieldValue(field: Field[_]): Seq[String] =  _getRawFieldValue(field.name)

  override def fields: List[Field[_]] = currentDate :: personaNomo :: familiaNomo :: retposxtadreso :: telefonnumero :: urbo ::
    ekskurso :: alveno :: esperanto :: komentoj :: Nil

  val currentDate = Field(
    name = "dato",
    caption = NoI18nString(""),
    visible = false,
    `type` = CurrentDateField()
  )
  val personaNomo = Field(
    name = "personaNomo",
    caption = I18nString(
      "eo" -> "Persona Nomo",
      "pl" -> "Imię"
    ),
    required = true,
    `type` = StringField()
  )

  val familiaNomo = Field(
    name = "familiaNomo",
    caption = I18nString(
      "eo" -> "Familia Nomo",
      "pl" -> "Nazwisko"
    ),
    required = true,
    `type` = StringField()
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
  val telefonnumero = Field(
    name = "telefonnumero",
    caption = I18nString(
      "eo" -> "Telefonnumero",
      "pl" -> "Telefon"
    ),
    description = Some(I18nString("eo" -> "Por povi kontaktiĝi", "pl" -> "W celu ewentualnego kontaktu")),
    `type` = StringField()
  )
  val urbo = Field(
    name = "urbo",
    caption = I18nString(
      "eo" -> "Urbo",
      "pl" -> "Miasto"
    ),
    `type` = StringField()
  )
  val ekskurso = Field(
    name = "ekskurso",
    caption = I18nString(
      "eo" -> "Ekskurso",
      "pl" -> "Wycieczka"
    ),
    description = Some(I18nString("eo" -> "nur informe", "pl" -> "tylko informacyjnie")),
    `type` = SelectField(
      options = List(
        EnumOption("nenio", I18nString(
          "eo" -> "mi ne scias/ne gravas",
          "pl" -> "nie wiem/nie ważne"
        )),
        EnumOption("bjalistoko", I18nString(
          "eo" -> "promenado tra Bjalistoko, vizito en la historia muzeo",
          "pl" -> "spacer po Białymstoku, wizyta w muzeum historycznym"
        )),
        EnumOption("suprasl", I18nString(
          "eo" -> "promenado tra Bjalistoko kaj Supraśl",
          "pl" -> "spacer po Białymstoku i wizyta w Supraślu"
        ))
      )
    )
  )
  val alveno = Field(
    name = "alveno",
    caption = I18nString(
      "eo" -> "Kiel kaj kiam vi alvenos kaj foriros?",
      "pl" -> "Kiedy i jak zamierzasz przyjechać i wyjechać?"
    ),
    description = Some(I18nString(
      "eo" -> "Tiu informo helpos al la organizantoj i.a. precizigi la programon",
      "pl" -> "Ta informacja pomoże organizatorom m.in. sprecyzowac program"
    )),
    `type` = StringField(textarea = true)
  )
  val esperanto = Field(
    name = "esperanto",
    caption = I18nString(
      "eo" -> "Nivelo de esperanto",
      "pl" -> "Poziom Esperanto"
    ),
    `type` = SelectField(List(
      EnumOption("novulo", I18nString("eo" -> "novulo", "pl" -> "nowy")),
      EnumOption("komencanto", I18nString("eo" -> "komencanto", "pl" -> "początkujący")),
      EnumOption("spertulo", I18nString("eo" -> "spertulo", "pl" -> "doświadczony")),
      EnumOption("specialisto", I18nString("eo" -> "specialisto", "pl" -> "specjalista"))
    ))
  )
  val komentoj = Field(
    name = "komentoj",
    caption = I18nString(
      "eo" -> "Komentoj, kontribuoj",
      "pl" -> "Komentarze, wkład"
    ),
    description = Some(I18nString(
      "eo" -> "Ekz. kontribuo al programo - preleg(et)o, disktrondo - kion vi ŝatus",
      "pl" -> "Np. Twój wkład w program - (krótka) prezentacja, dyskusja itp"
    )),
    `type` = StringField(textarea = true)
  )
}