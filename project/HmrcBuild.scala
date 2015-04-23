import sbt.Keys._
import sbt._
import uk.gov.hmrc.SbtAutoBuildPlugin

object HmrcBuild extends Build {

  import BuildDependencies._
  import uk.gov.hmrc.DefaultBuildSettings._

  val versionApp = "1.0.0"
  val appName = "reference-checker"

  lazy val referenceChecker = (project in file("."))
    .enablePlugins(SbtAutoBuildPlugin)
    .settings(
      name := appName,
      targetJvm := "jvm-1.7",
      version := versionApp,
      libraryDependencies ++= Seq(Test.scalaTest, Test.scalaCheck, Test.pegdown),
      crossScalaVersions := Seq("2.11.6"),
      ArtefactDescription()
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

object ArtefactDescription {

  def apply() = Seq(
    pomExtra := (<url>https://www.gov.uk/government/organisations/hm-revenue-customs</url>
      <licenses>
        <license>
          <name>Apache 2</name>
          <url>http://www.apache.org/licenses/LICENSE-2.0.txt</url>
        </license>
      </licenses>
      <scm>
        <connection>scm:git@github.com:hmrc/reference-checker.git</connection>
        <developerConnection>scm:git@github.com:hmrc/reference-checker.git</developerConnection>
        <url>git@github.com:hmrc/reference-checker.git</url>
      </scm>)
    )
}
