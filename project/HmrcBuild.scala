import sbt.Keys._
import sbt._
import uk.gov.hmrc.SbtAutoBuildPlugin
import uk.gov.hmrc.versioning.SbtGitVersioning

object HmrcBuild extends Build {

  val appName = "reference-checker"

  lazy val microservice = Project(appName, file("."))
    .enablePlugins(SbtAutoBuildPlugin, SbtGitVersioning)
    .settings(
      scalaVersion := "2.11.7",
      libraryDependencies ++= BuildDependencies(),
      crossScalaVersions := Seq("2.11.7")
    )
    .disablePlugins(sbt.plugins.JUnitXmlReportPlugin)

}

private object BuildDependencies {

  lazy val testDeps = Seq(
    "org.scalatest" %% "scalatest" % "2.2.4" % "test",
    "org.scalacheck" %% "scalacheck" % "1.12.2" % "test",
    "org.pegdown" % "pegdown" % "1.5.0" % "test"
  )

  def apply() = testDeps

}