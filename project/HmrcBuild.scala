import sbt.Keys._
import sbt._
import uk.gov.hmrc.SbtAutoBuildPlugin
import uk.gov.hmrc.versioning.SbtGitVersioning.autoImport.majorVersion
import uk.gov.hmrc.versioning.SbtGitVersioning
import uk.gov.hmrc.SbtArtifactory
import uk.gov.hmrc.SbtArtifactory.autoImport.makePublicallyAvailableOnBintray


object HmrcBuild extends Build {

  val appName = "reference-checker"
  lazy val plugins : Seq[Plugins] = Seq(play.sbt.PlayScala)

  lazy val microservice = Project(appName, file("."))
    .enablePlugins(Seq(SbtAutoBuildPlugin, SbtGitVersioning, SbtArtifactory) : _*)
    .settings(majorVersion := 2)
    .settings(makePublicallyAvailableOnBintray := true)
    .enablePlugins(Seq(play.sbt.PlayScala,SbtAutoBuildPlugin, SbtGitVersioning) ++ plugins : _*)
    .settings(
      scalaVersion := "2.11.11",
      libraryDependencies ++= BuildDependencies(),
      crossScalaVersions := Seq("2.11.11")
    )

    .disablePlugins(sbt.plugins.JUnitXmlReportPlugin)

}

private object BuildDependencies {

  lazy val testDeps = Seq(
    "org.scalatest" %% "scalatest" % "3.0.0" % "test",
    "org.scalacheck" %% "scalacheck" % "1.13.4" % "test",
    "org.pegdown" % "pegdown" % "1.6.0" % "test"
  )

  def apply() = testDeps

}
