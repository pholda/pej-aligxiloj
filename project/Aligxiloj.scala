
import org.scalajs.sbtplugin.ScalaJSPlugin
import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._
import sbt._
import Keys._

object Aligxiloj extends Build {
  val maVersion = "0.1.1-SNAPSHOT"

  val defaults = Defaults.coreDefaultSettings ++ List(
    organization := "pl.pej.aligxiloj",
    version := "0.1-SNAPSHOT",
    scalaVersion := "2.11.5",
    libraryDependencies ++= List(
      "org.scalatest" % "scalatest_2.11" % "2.2.3" % "test",
      "pl.pej.malpompaaligxilo" %% "core_sjs0.6" % maVersion
    )
  )

  lazy val root = Project(id = "pej-aligxiloj",
    base = file("."),
    settings = defaults ++ List(
      name := "pej-aligxiloj",
      publishArtifact := false
    )
  ).aggregate(jes2015, playBackend, semajnfino)

  lazy val jes2015 = Project(id = "jes2015",
    base = file("jes2015"),
    settings = defaults ++ List(
      name := "jes2015",
      libraryDependencies ++= Seq(
        "be.doeraene" %%% "scalajs-jquery" % "0.8.0",
        "joda-time" % "joda-time" % "2.0",
        "com.github.nscala-time" %% "nscala-time" % "1.6.0"
      )//,
//      skip in ScalaJSKeys.packageJSDependencies := false
    )
  ).enablePlugins(ScalaJSPlugin)

  lazy val semajnfino = Project(id = "semajnfino",
    base = file("semajnfino"),
    settings = defaults ++ List(
      name := "semajnfino"//,
//      libraryDependencies ++= Seq(
//        "joda-time" % "joda-time" % "2.0"
//      )//,
//      skip in ScalaJSKeys.packageJSDependencies := false
    )
  ).dependsOn().enablePlugins(ScalaJSPlugin)

  lazy val playBackend = Project(
    id = "playBackend",
    base = file("playBackend"),
    settings = defaults ++ List(
      name := "playBackend",
      libraryDependencies ++= Seq(
        "com.typesafe.play.plugins" %% "play-plugins-mailer" % "2.3.6-SNAPSHOT",
        "org.mongodb" %% "casbah" % "2.7.4",
        "joda-time" % "joda-time" % "2.0",
        "com.github.nscala-time" %% "nscala-time" % "1.6.0",
        "pl.pej.malpompaaligxilo" %% "twirl-templates" % maVersion,
        "pl.pej.malpompaaligxilo" %% "google-api" % maVersion
      )
    )
  ).dependsOn(jes2015, semajnfino).enablePlugins(play.PlayScala)
}
