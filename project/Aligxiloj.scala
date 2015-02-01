import sbt._
import Keys._
import scalajs.sbtplugin.ScalaJSPlugin._

object Aligxiloj extends Build {
  val defaults = Defaults.coreDefaultSettings ++ List(
    organization := "pl.pej.aligxiloj",
    version := "0.1-SNAPSHOT",
    scalaVersion := "2.11.1",
    libraryDependencies ++= List(
      "org.scalatest" % "scalatest_2.11" % "2.2.3" % "test",
      "pl.pej.malpompaaligxilo" %% "core" % "0.1-SNAPSHOT"
    )
  )

  lazy val root = Project(id = "aligxiloj",
    base = file("."),
    settings = Defaults.defaultSettings ++ List(
      name := "aligxiloj",
      scalaVersion := "2.11.1",
      publishArtifact := false
    )
  ).aggregate(jes2015, playBackend, semajnfino)

  lazy val jes2015 = Project(id = "jes2015",
    base = file("jes2015"),
    settings = defaults ++ scalaJSSettings ++ List(
      name := "jes2015",
      libraryDependencies ++= Seq(
        "org.scala-lang.modules.scalajs" %%% "scalajs-jquery" % "0.6",
        "pl.pej.malpompaaligxilo" %%% "scalajs" % "0.1-SNAPSHOT",
        "joda-time" % "joda-time" % "2.0",
        "com.github.nscala-time" %% "nscala-time" % "1.6.0"
      ),
      skip in ScalaJSKeys.packageJSDependencies := false
    )
  )

  lazy val semajnfino = Project(id = "semajnfino",
    base = file("semajnfino"),
    settings = defaults ++ scalaJSSettings ++ List(
      name := "semajnfino",
      libraryDependencies ++= Seq(
        "org.scala-lang.modules.scalajs" %%% "scalajs-jquery" % "0.6",
        "pl.pej.malpompaaligxilo" %%% "scalajs" % "0.1-SNAPSHOT",
        "joda-time" % "joda-time" % "2.0",
        "com.github.nscala-time" %% "nscala-time" % "1.6.0"
      ),
      skip in ScalaJSKeys.packageJSDependencies := false
    )
  ).dependsOn()

  lazy val playBackend = Project(
    id = "playBackend",
    base = file("playBackend"),
    settings = defaults ++ List(
      name := "playBackend",
      libraryDependencies ++= Seq(
        "com.typesafe.play.plugins" %% "play-plugins-mailer" % "2.3.6-SNAPSHOT",
        "org.mongodb" %% "casbah" % "2.7.4",
        "joda-time" % "joda-time" % "2.0",
        "com.github.nscala-time" %% "nscala-time" % "1.6.0"
      )
    )
  ).dependsOn(jes2015, semajnfino).enablePlugins(play.PlayScala)
}
