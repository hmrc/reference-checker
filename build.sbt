import sbt.Keys._
import sbt.{Compile, Def, EvictionWarningOptions, Project, Resolver, Test, file, _}
import uk.gov.hmrc.versioning.SbtGitVersioning.autoImport.majorVersion
import uk.gov.hmrc.versioning.SbtGitVersioning
import uk.gov.hmrc.{SbtArtifactory, SbtAutoBuildPlugin, ShellPrompt, _}
import com.typesafe.sbt.SbtScalariform.ScalariformKeys
import uk.gov.hmrc.DefaultBuildSettings.scalaSettings
import scalariform.formatter.preferences._

val appName = "reference-checker"

lazy val referenceCheckerLib = Project(appName, file("."))
  .enablePlugins(
    SbtAutoBuildPlugin,
    SbtGitVersioning,
    SbtArtifactory
  )
  .settings(
    Seq(
      crossScalaVersions := supportedScalaVersions,
      majorVersion := 2,
      scalacOptions ++= scalaCompilerOptions,
      resolvers ++= Seq(Resolver.bintrayRepo("hmrc", "releases"), Resolver.jcenterRepo),
      evictionWarningOptions in update := EvictionWarningOptions.default.withWarnScalaVersionEviction(false),
      wartremoverExcluded ++= Seq(sourceManaged.value / "main" / "sbt-buildinfo" / "BuildInfo.scala"),
      scalariformSettings,
      shellPrompt := ShellPrompt(version.value),
      buildInfoPackage := name.value.toLowerCase().replaceAllLiterally("-", "")
    ).++(wartRemoverError)
      .++(wartRemoverWarning)
//      .++(Seq(
//        wartremoverErrors in(Test, compile) --= Seq(Wart.Any, Wart.Equals, Wart.Null, Wart.NonUnitStatements, Wart.PublicInference)
//      ))
      .++(scoverageSettings)
      .++(scalaSettings)
      .++(uk.gov.hmrc.DefaultBuildSettings.defaultSettings()): _ *
  )
  .settings(
    libraryDependencies ++= List(
      "org.scalatest" %% "scalatest" % "3.0.0" % "test",
      "org.scalacheck" %% "scalacheck" % "1.13.4" % "test",
      "org.pegdown" % "pegdown" % "1.6.0" % "test"
    )
  )

lazy val scala212 = "2.12.12"
lazy val scala211 = "2.11.11"
lazy val supportedScalaVersions = List(scala212, scala211)

val scalaCompilerOptions = Seq(
  "-Xfatal-warnings",
  "-Xlint:-missing-interpolator,_",
  "-Yno-adapted-args",
  "-Ywarn-value-discard",
  "-Ywarn-dead-code",
  "-deprecation",
  "-feature",
  "-unchecked",
  "-language:implicitConversions",
  "-Ypartial-unification" //required by cats
)

lazy val scalariformSettings: Def.SettingsDefinition = {
  // description of options found here -> https://github.com/scala-ide/scalariform
  ScalariformKeys.preferences := ScalariformKeys.preferences.value
    .setPreference(AlignArguments, true)
    .setPreference(AlignParameters, true)
    .setPreference(AlignSingleLineCaseStatements, true)
    .setPreference(AllowParamGroupsOnNewlines, true)
    .setPreference(CompactControlReadability, false)
    .setPreference(CompactStringConcatenation, false)
    .setPreference(DanglingCloseParenthesis, Force)
    .setPreference(DoubleIndentConstructorArguments, true)
    .setPreference(DoubleIndentMethodDeclaration, true)
    .setPreference(FirstArgumentOnNewline, Force)
    .setPreference(FirstParameterOnNewline, Force)
    .setPreference(FormatXml, true)
    .setPreference(IndentLocalDefs, true)
    .setPreference(IndentPackageBlocks, true)
    .setPreference(IndentSpaces, 2)
    .setPreference(IndentWithTabs, false)
    .setPreference(MultilineScaladocCommentsStartOnFirstLine, false)
    .setPreference(NewlineAtEndOfFile, true)
    .setPreference(PlaceScaladocAsterisksBeneathSecondAsterisk, false)
    .setPreference(PreserveSpaceBeforeArguments, true)
    .setPreference(RewriteArrowSymbols, false)
    .setPreference(SpaceBeforeColon, false)
    .setPreference(SpaceBeforeContextColon, false)
    .setPreference(SpaceInsideBrackets, false)
    .setPreference(SpaceInsideParentheses, false)
    .setPreference(SpacesAroundMultiImports, false)
    .setPreference(SpacesWithinPatternBinders, true)
}


lazy val wartRemoverWarning = {
  val warningWarts = Seq()
  wartremoverWarnings in(Compile, compile) ++= warningWarts
}


lazy val wartRemoverError = {
  // Error
  val errorWarts = Seq(
    Wart.ArrayEquals,
    Wart.AnyVal,
    Wart.EitherProjectionPartial,
    Wart.Enumeration,
    Wart.ExplicitImplicitTypes,
    Wart.FinalVal,
    Wart.JavaConversions,
    Wart.JavaSerializable,
    Wart.LeakingSealed,
    Wart.MutableDataStructures,
    Wart.Null,
    Wart.OptionPartial,
    Wart.Recursion,
    Wart.Return,
    Wart.TraversableOps,
    Wart.TryPartial,
    Wart.Var,
    Wart.While,
    Wart.FinalCaseClass,
    Wart.JavaSerializable,
    Wart.StringPlusAny,
    Wart.AsInstanceOf,
    Wart.IsInstanceOf,
    Wart.Any
  )
  wartremoverErrors in(Compile, compile) ++= errorWarts
}

lazy val scoverageSettings = {
  import scoverage.ScoverageKeys
  Seq(
    // Semicolon-separated list of regexs matching classes to exclude
    ScoverageKeys.coverageExcludedPackages := "<empty>;.*BuildInfo.*;Reverse.*;app.Routes.*;prod.*;testOnlyDoNotUseInProd.*;manualdihealth.*;forms.*;config.*;",
    ScoverageKeys.coverageExcludedFiles := ".*microserviceGlobal.*;.*microserviceWiring.*;.*ApplicationLoader.*;.*ApplicationConfig.*;.*package.*;.*Routes.*;.*TestOnlyController.*;.*WebService.*",
    ScoverageKeys.coverageMinimum := 80,
    ScoverageKeys.coverageFailOnMinimum := false,
    ScoverageKeys.coverageHighlighting := true
  )
}

