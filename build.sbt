import sbt._
import scoverage.ScoverageKeys
import com.typesafe.sbt.SbtScalariform.ScalariformKeys
import scalariform.formatter.preferences._
import uk.gov.hmrc.DefaultBuildSettings.scalaSettings
import uk.gov.hmrc.DefaultBuildSettings.{defaultSettings, integrationTestSettings, scalaSettings}

val appName = "reference-checker"

lazy val scala213 = "2.13.8"
lazy val scala212 = "2.12.16"
lazy val supportedScalaVersions = List(scala213, scala212)

lazy val referenceCheckerLib = Project(appName, file("."))
  .enablePlugins(SbtAutoBuildPlugin, SbtGitVersioning)
  .settings(scalaVersion := scala213)
  .settings(
    Seq(
      crossScalaVersions := supportedScalaVersions,
      majorVersion := 2,
      scalacOptions ++= scalaCompilerOptions,
      resolvers ++= Seq(Resolver.jcenterRepo),
      update / evictionWarningOptions := EvictionWarningOptions.default.withWarnScalaVersionEviction(false),
      wartremoverExcluded ++= Seq(sourceManaged.value / "main" / "sbt-buildinfo" / "BuildInfo.scala"),
      scalariformSettings,
      buildInfoPackage := name.value.toLowerCase().replaceAllLiterally("-", "")
    ).++(wartRemoverError)
      .++(wartRemoverWarning)
      .++(scoverageSettings)
      .++(scalaSettings)
      .++(uk.gov.hmrc.DefaultBuildSettings.defaultSettings()): _ *
  )
  .settings(
    libraryDependencies ++= List(
      "org.scalatest"        %% "scalatest"       % "3.2.15",
      "org.scalatestplus"    %% "scalacheck-1-14" % "3.1.1.1",
      "org.scalacheck"       %% "scalacheck"      % "1.15.2",
      "org.pegdown"          %  "pegdown"         % "1.6.0",
      "com.vladsch.flexmark" %  "flexmark-all"    % "0.62.2"
    ).map(_ % Test)
  )
  .settings(
    Compile / scalacOptions ++= {
      CrossVersion.partialVersion(scalaVersion.value) match {
        case Some((2, n)) if n <= 12 => List("-Yno-adapted-args", "-deprecation", "-Xfatal-warnings")
        case _                       => List("-Ymacro-annotations", "-Wconf:cat=deprecation:ws")
      }
    }
  )

val scalaCompilerOptions = Seq(
  "-Xlint:-missing-interpolator,_",
  "-Ywarn-value-discard",
  "-Ywarn-dead-code",
  "-feature",
  "-unchecked",
  "-language:implicitConversions"
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
  Compile / compile / wartremoverWarnings ++= warningWarts
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
  Compile / compile / wartremoverErrors ++= errorWarts
}

lazy val scoverageSettings = {
  import scoverage.ScoverageKeys
  Seq(
    // Semicolon-separated list of regexs matching classes to exclude
    ScoverageKeys.coverageExcludedPackages := "<empty>;.*BuildInfo.*;Reverse.*;app.Routes.*;prod.*;testOnlyDoNotUseInProd.*;manualdihealth.*;forms.*;config.*;",
    ScoverageKeys.coverageExcludedFiles := ".*microserviceGlobal.*;.*microserviceWiring.*;.*ApplicationLoader.*;.*ApplicationConfig.*;.*package.*;.*Routes.*;.*TestOnlyController.*;.*WebService.*",
    ScoverageKeys.coverageMinimumStmtTotal := 80,
    ScoverageKeys.coverageFailOnMinimum := false,
    ScoverageKeys.coverageHighlighting := true
  )
}

