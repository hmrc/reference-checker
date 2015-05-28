import sbt.Keys._
import sbt._
import uk.gov.hmrc.SbtAutoBuildPlugin
import uk.gov.hmrc.versioning.SbtGitVersioning

object HmrcBuild extends Build {

  import BuildDependencies._
  import uk.gov.hmrc.DefaultBuildSettings._

  val appName = "reference-checker"

  lazy val referenceChecker = (project in file("."))
    .enablePlugins(SbtAutoBuildPlugin, SbtGitVersioning)
    .settings(
      name := appName,
      targetJvm := "jvm-1.7",
      libraryDependencies ++= Seq(Test.scalaTest, Test.scalaCheck, Test.pegdown),
      crossScalaVersions := Seq("2.11.6")
    )
}

private object BuildDependencies {

  object Compile {
  }

  sealed abstract class Test(scope: String) {
    val scalaTest   = "org.scalatest" %% "scalatest" % "2.2.4" % scope
    val scalaCheck  = "org.scalacheck" %% "scalacheck" % "1.12.2" % scope
    val pegdown = "org.pegdown" % "pegdown" % "1.5.0" % scope cross CrossVersion.Disabled

  }

  object Test extends Test("test")

}