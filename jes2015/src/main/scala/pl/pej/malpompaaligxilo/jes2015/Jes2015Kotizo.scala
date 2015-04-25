package pl.pej.malpompaaligxilo.jes2015

import pl.pej.malpompaaligxilo.util._

import scala.collection.immutable.ListMap
import scala.util.Try

object Jes2015Kotizo {
  type Euroj = Double

  implicit def bool2int(b:Boolean): Int = if (b) 1 else 0

  object Prezoj {

    /** la nomoj ne spegulas precize la limdatojn,
      * sed tiel pli facile kompreni kaj eviti eraron
      */
    val IJF: Seq[Seq[Euroj]] = Seq(Seq(0,0,0),Seq(10,-25,-35),Seq(30,0,-10),Seq(70,50,20))
    val IJS: Seq[Seq[Euroj]] = Seq(Seq(0,0,0),Seq(20,-15,-25),Seq(40,10,0),Seq(80,60,30))
    val finoDeSomero: Seq[Seq[Euroj]]= Seq(Seq(0,0,0),Seq(30,-5,-15),Seq(50,20,10),Seq(90,70,40))
    val novembro: Seq[Seq[Euroj]] = Seq(Seq(0,0,0),Seq(40,10,0),Seq(60,30,20),Seq(100,80,60))
    val decembro: Seq[Seq[Euroj]] = Seq(Seq(0,0,0),Seq(60,30,20),Seq(80,50,40),Seq(120,100,80))


    val matenmangxo: Euroj = 4
    val tagmangxo: Euroj = 5
    val vespermangxo: Euroj = 4


    val tranoktoAmasejo: Euroj = 5
    val tranoktoSendusxeja: Euroj = 8
    val tranoktoKundusxeja: Euroj = 10

    val dulitaKrompago: Euroj = 40

    val imposxto: Euroj = 1.5

    val invitletero: Euroj = 5

    val balo: Seq[Euroj] = Seq(0, 10, 15, 30)

    /** (rabato) */
    val tutaPagoGxisFinoDeSomero: Euroj = 5
  }

  def kotizo(implicit form: Jes2015Aligxilo, poCfg: PoCfg): Kotizo = {
    Try {

      import form._


      lazy val studento_? = studento.value == Some(true)


      lazy val cxeesto: Set[String] = cxeestoElekto.value.get.map {
        case (row, col) => row.id
      }.toSet

      lazy val noktoj: Int = {

        if (form.cxeesto.value.exists(_.value == "cxiun")) {
          7
        } else if (form.cxeesto.value.exists(_.value == "balo")) {
          1
        } else {
          cxeesto("27/28").toInt + cxeesto("28/29").toInt + cxeesto("29/30").toInt + cxeesto("30/31").toInt + cxeesto("31/1").toInt + cxeesto("1/2").toInt + cxeesto("2/3").toInt
        }
      }

      lazy val programo: Map[KotizoComponent, Euroj] = {
        val currentDate = form.dates.now

        val aligxkategoriajPrezoj: Seq[Seq[Euroj]] = if (currentDate < form.dates.fromString("2015-04-30")) Prezoj.IJF
        else if (currentDate < form.dates.fromString("2015-06-30")) Prezoj.IJS
        else if (currentDate < form.dates.fromString("2015-08-31")) Prezoj.finoDeSomero
        else if (currentDate < form.dates.fromString("2015-10-31")) Prezoj.novembro
        else Prezoj.decembro

        val naskiita = form.dates.fromString(naskigxdato.value.get.toString)

        val agxKategorio = if (naskiita > form.dates.fromString("2000-12-26")) 0
        else if (naskiita > form.dates.fromString("1985-12-26") && studento_?) 1
        else if (naskiita > form.dates.fromString("1985-12-26")) 2
        else 3

        val lando = form.lando.value.get.value

        val aLandoj = Seq("ad", "at", "bh", "be", "gb", "dk", "fi", "fr", "de", "ie", "is", "il", "it", "jp", "ca", "qa", "kw", "li", "lu", "mc", "nl", "no", "om", "sm", "sa", "se", "ch", "ae", "us")

        val bLandoj = Seq(
          "al", "au", "ba", "bn", "bg", "cz", "ee", "gr", "es", "hu", "cy", "hr", "lt", "lv", "mk", "mt", "me", "nz", "pl", "pt", "ru", "rs", "sg", "sk", "si", "kr", "tw", "tr")

        val landoKategorio = if (aLandoj.contains(lando)) 0 else if (bLandoj.contains(lando)) 1 else 2

        val programo = aligxkategoriajPrezoj(agxKategorio)(landoKategorio) * scala.math.min(1 + noktoj, 7).toDouble / 7

        Map(SingleKotizoComponent(I18n.po("Programkotizo")) -> programo)
      }

      val mangxado: Map[KotizoComponent, Euroj] = {

        val matenmangxo: Boolean = form.matenmangxoj.value.getOrElse(false)
        val tagmangxo: Boolean = form.tagmangxoj.value.getOrElse(false)
        val vespermangxo: Boolean = form.vespermangxoj.value.getOrElse(false)

        val kiomMatenmangxoj = noktoj
        val kiomTagmangxoj = math.max(0, noktoj - 1)
        val kiomVespermangxoj = noktoj

        val prezoMatenmangxo = Prezoj.matenmangxo * matenmangxo.toInt
        val prezoTagmangxo = Prezoj.tagmangxo * tagmangxo.toInt
        val prezoVespermangxo = Prezoj.vespermangxo * vespermangxo.toInt

        Map(
          MultimpleKotizoComponent(I18n.po("Matenmanĝoj"), prezoMatenmangxo, kiomMatenmangxoj) -> kiomMatenmangxoj * prezoMatenmangxo,
          MultimpleKotizoComponent(I18n.po("Tagmanĝoj"), prezoTagmangxo, kiomTagmangxoj) -> kiomTagmangxoj * prezoTagmangxo,
          MultimpleKotizoComponent(I18n.po("Vespermanĝoj"), prezoVespermangxo, kiomVespermangxoj) -> kiomVespermangxoj * prezoVespermangxo)
      }

      val logxado: Map[KotizoComponent, Euroj] = {

        val logxelekto = form.logxado.value.get.value

        logxelekto match {
          case "2-lita-cxambro" => Map(
            MultimpleKotizoComponent(I18n.po("Ĉambro kun propra duŝejo"), Prezoj.tranoktoKundusxeja, noktoj) -> noktoj * Prezoj.tranoktoKundusxeja,
            SingleKotizoComponent(I18n.po("Dulita krompago")) -> Prezoj.dulitaKrompago
          )
          case "kundusxeja" => Map(
            MultimpleKotizoComponent(I18n.po("Ĉambro kun propra duŝejo (4-6 persona)"), Prezoj.tranoktoKundusxeja, noktoj) -> noktoj * Prezoj.tranoktoKundusxeja
          )
          case "4-lita-cxambro-sen-dusxejo" => Map(
            MultimpleKotizoComponent(I18n.po("Ĉambro kun koridora duŝejo"), Prezoj.tranoktoSendusxeja, noktoj) -> noktoj * Prezoj.tranoktoSendusxeja
          )
          //        case "14-lita-cxambro-sen-dusxejo" => Map(
          //          s"Ĉambro kun koridora duŝejo ($noktoj&nbsp;foje&nbsp;%.2f)".format(prezoSendusxejo) -> noktoj * prezoSendusxejo
          //        )
//          case "memzorganto" => Map.empty
          case _: String => Map(
            MultimpleKotizoComponent(I18n.po("Amasejo"), Prezoj.tranoktoAmasejo, noktoj) -> noktoj * Prezoj.tranoktoAmasejo
          )
        }
      }

      val invitletero: Map[KotizoComponent, Euroj] = {

        val invitletero_? : Boolean = invitilo.value.exists(_.value == "jes")

        Map(SingleKotizoComponent(I18n.po("Invitletero")) -> Prezoj.invitletero * (if (invitletero_?) 1 else 0))
      }

      val imposto: ListMap[KotizoComponent, Euroj] = {

        val naskiita = form.dates.fromString(form.naskigxdato.value.get.toString)

        if (naskiita <= form.dates.fromString("1997-12-27") && form.logxado.value.exists(_.value != "memzorganto"))
          ListMap(
            MultimpleKotizoComponent(I18n.po("Imposto por hungara ŝtato"), Prezoj.imposxto, noktoj) -> Prezoj.imposxto * noktoj
          )
        else ListMap()
      }

      val balo: ListMap[KotizoComponent, Euroj] = {

        val pagasBalokotizon_? = (cxeesto.apply("31/1") && noktoj == 1) || form.cxeesto.value.exists(_.value == "balo")

        if (pagasBalokotizon_?) {
          val naskiita = naskigxdato.value.get

          val agxKategorio = if (naskiita > form.dates.fromString("2000-12-26")) 0
          else if (naskiita > form.dates.fromString("1985-12-26") && studento_?) 1
          else if (naskiita > form.dates.fromString("1985-12-26")) 2
          else 3

          ListMap(SingleKotizoComponent(I18n.po("Balokotizo")) -> Prezoj.balo(agxKategorio))
        } else ListMap()
      }

      val donaco: ListMap[KotizoComponent, Euroj] = {
        val kvoto = donacoKvoto.value.getOrElse(0)
        kvoto match {
          case s if s > 0 =>
            ListMap(
              SingleKotizoComponent(I18n.po("Donaco al JES 2015")) -> s
            )
          case s => ListMap.empty
        }
      }

      val tutpagaRabato: ListMap[KotizoComponent, Euroj] = {
        val tutpagaRabato_? : Boolean = miPagos.value.exists(_.value == "tuton")
        val suficxeFrue_? : Boolean = form.dates.now < form.dates.fromString("2015-08-31")

        if (tutpagaRabato_? && suficxeFrue_?) ListMap(
          SingleKotizoComponent(I18n.po("Rabato pro bonvola tuja tuta pago (dankon!)")) -> -Prezoj.tutaPagoGxisFinoDeSomero
        )
        else ListMap.empty
      }

      val finaPrezo: ListMap[KotizoComponent, Euroj] = ListMap() ++ mangxado ++ logxado ++ imposto ++ programo ++ invitletero ++ balo ++ tutpagaRabato ++ donaco
      Kotizo(finaPrezo)
    }.getOrElse(Kotizo(ListMap.empty))
  }



}
