import sbt._
import Keys._
import sbt.Keys._
import play.Project._

object PlaySDKBuild extends Build {

  lazy val sdk = play.Project("sphere-sdk").aggregate(spherePlaySDK)

  // ----------------------
  // Play SDK
  // ----------------------

  lazy val spherePlaySDK = play.Project(
    "sphere-play-sdk",
    "0.25-SNAPSHOT",
    Seq(javaCore),
    path = file("play-sdk")  
  ).dependsOn(sphereJavaClient % "compile->compile;test->test").
    // aggregate: clean, compile, publish etc. transitively
    aggregate(sphereJavaClient).
    settings(standardSettings:_*).
    settings(scalaSettings:_*).
    settings(java6Settings:_*).
    settings(testSettings(Libs.scalatest, Libs.scalamock):_*).
    settings(publishSettings:_*)

  // ----------------------
  // Java client
  // ----------------------

  lazy val sphereJavaClient = Project(
    id = "sphere-java-client",
    base = file("java-client"),
    settings =
      Defaults.defaultSettings ++ standardSettings ++ scalaSettings ++ java6Settings ++
      testSettings(Libs.scalatest, Libs.scalamock) ++ publishSettings ++ Seq(
        version := "0.25-SNAPSHOT",
        autoScalaLibrary := true, // no dependency on Scala standard library (just for tests)
        crossPaths := false,
        libraryDependencies ++= Seq(
          Libs.asyncHttpClient, Libs.guava, Libs.jodaTime, Libs.jodaConvert,
          Libs.jackson, Libs.jacksonMapper, Libs.jcip,
          Libs.commonsCodec, // Base64 for OAuth client
          Libs.nvI18n        // CountryCode
        )))

  // ----------------------
  // Settings
  // ----------------------

  lazy val standardSettings = Seq[Setting[_]](
    organization := "io.sphere",
    // Don't publish Scaladoc
    publishArtifact in (Compile, packageDoc) := false
  )

  lazy val scalaSettings = Seq[Setting[_]](
    scalaVersion := "2.10.0",
    // Emit warnings for deprecated APIs, emit erasure warnings
    scalacOptions ++= Seq("-deprecation", "-unchecked")
  )

  // Compile the SDK for Java 6, for developers who're still on Java 6
  lazy val java6Settings = Seq[Setting[_]](
    // Emit warnings for deprecated APIs, emit erasure warnings
    javacOptions ++= Seq("-deprecation", "-Xlint:unchecked", "-source", "1.6", "-target", "1.6"),
    // javadoc options
    javacOptions in doc := Seq("-source", "1.6")
  )

  def testSettings(testLibs: ModuleID*) = Seq[Setting[_]](
    parallelExecution in Test := false,
    libraryDependencies ++= Seq(testLibs:_*),
    testOptions in Test := Seq(
      //Tests.Argument(TestFrameworks.ScalaTest, "-l", "disabled integration"),
      Tests.Argument(TestFrameworks.ScalaTest, "-oD")) // show durations
  )

  // To 'sbt publish' to commercetools public Nexus
  lazy val publishSettings = Seq(
    credentials ++= Seq(
      Credentials(Path.userHome / ".ivy2" / ".ct-credentials"),
      Credentials(Path.userHome / ".ivy2" / ".ct-credentials-public")),
    publishTo <<= (version) { version: String =>
      if(version.trim.endsWith("SNAPSHOT"))
        Some("ct-snapshots" at "http://repo.ci.cloud.commercetools.de/content/repositories/snapshots")
      else
        Some("ct-public-releases" at "http://public-repo.ci.cloud.commercetools.de/content/repositories/releases")
    })

  // ----------------------
  // Dependencies
  // ----------------------

  object Libs {
    lazy val asyncHttpClient = "com.ning" % "async-http-client" % "1.7.5"
    lazy val guava           = "com.google.guava" % "guava" % "12.0"
    lazy val jodaTime        = "joda-time" % "joda-time" % "2.1"
    lazy val jodaConvert     = "org.joda" % "joda-convert" % "1.1"
    lazy val jackson         = "org.codehaus.jackson" % "jackson-core-asl" % "1.9.9"
    lazy val jacksonMapper   = "org.codehaus.jackson" % "jackson-mapper-asl" % "1.9.9"
    lazy val jcip            = "net.jcip" % "jcip-annotations" % "1.0"
    lazy val commonsCodec    = "commons-codec" % "commons-codec" % "1.5"
    lazy val nvI18n          = "com.neovisionaries" % "nv-i18n" % "1.4"

    lazy val scalatest       = "org.scalatest" %% "scalatest" % "1.7.1" % "test"
    lazy val scalamock       = "org.scalamock" %% "scalamock-scalatest-support" % "3.0.1"
  }
}