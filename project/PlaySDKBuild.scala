import sbt._
import Keys._
import play.Project._
import com.typesafe.sbteclipse.core.EclipsePlugin
import Release._

import de.johoop.jacoco4sbt._
import JacocoPlugin._

object PlaySDKBuild extends Build {

  lazy val sdk = play.Project("sphere-sdk").
                  settings(standardSettings:_*).
                  aggregate(spherePlaySDK)

  // ----------------------
  // Play SDK
  // ----------------------

  lazy val spherePlaySDK = play.Project(
    "sphere-play-sdk",
    dependencies = Seq(javaCore),
    path = file("play-sdk")
  ).dependsOn(sphereJavaClient % "compile->compile;test->test;it->it").
    // aggregate: clean, compile, publish etc. transitively
    aggregate(sphereJavaClient).
    settings(standardSettings:_*).
    settings(scalaSettings:_*).
    settings(java6Settings:_*).
    settings(testSettings(Libs.scalatest, Libs.playTest, Libs.play):_*).
    configs(IntegrationTest).
    settings(scalaSource in IntegrationTest <<= baseDirectory (_ / "it"))

  // ----------------------
  // Java client
  // ----------------------

  lazy val sphereJavaClient = Project(
    id = "sphere-java-client",
    base = file("java-client"),
    settings =
      Defaults.defaultSettings ++ standardSettings ++ scalaSettings ++ java6Settings ++ Defaults.itSettings ++
        testSettings(Libs.scalatest, Libs.logbackClassic, Libs.logbackCore) ++ Seq(
        autoScalaLibrary := false, // no dependency on Scala standard library (just for tests)
        crossPaths := false,
        libraryDependencies ++= Seq(
          Libs.asyncHttpClient, Libs.guava, Libs.jodaTime, Libs.jodaConvert,
          Libs.jackson, Libs.jacksonMapper, Libs.jcip,
          Libs.nvI18n        // CountryCode
        ),
        sourceGenerators in Compile <+= sourceManaged in Compile map { outDir: File =>
          val file = outDir / "io" / "sphere" / "internal" / "Version.java"
          IO.write(file, """
package io.sphere.internal;

/** Current version of the Sphere Java client, useful for User Agent HTTP header, logging, etc. */
// Don't edit this file - it is autogenerated by sbt
public final class Version {
    public static final String version = """" + version + """";
}
""")
          Seq(file)
        }
      )).configs(IntegrationTest)

  // ----------------------
  // Settings
  // ----------------------

  lazy val standardSettings = publishSettings ++ Seq(
    organization := "io.sphere",
    version <<= version in ThisBuild,
    licenses := Seq("Apache" -> url("http://www.apache.org/licenses/LICENSE-2.0")),
    homepage := Some(url("https://github.com/commercetools/sphere-play-sdk"))
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

  def testSettings(testLibs: ModuleID*): Seq[Setting[_]] = {
     Defaults.itSettings ++ Seq(Test, jacoco.Config, IntegrationTest).map { testScope: Configuration =>
       Seq[Setting[_]](
         parallelExecution in testScope := false,
         libraryDependencies ++= Seq(testLibs:_*),
         testOptions in testScope <<= (target in testScope) map { target => Seq(
           //Tests.Argument(TestFrameworks.ScalaTest, "-l", "disabled integration"),
           Tests.Argument(TestFrameworks.ScalaTest, "-oD"), // show durations
           Tests.Argument(TestFrameworks.ScalaTest, "junitxml(directory=\"%s\")" format (target / "test-reports")))
         }
       )
     }.flatten ++ jacoco.settings
  }

  // ----------------------
  // Dependencies
  // ----------------------

  object Libs {
    lazy val asyncHttpClient = "com.ning" % "async-http-client" % "1.7.16"
    lazy val guava           = "com.google.guava" % "guava" % "12.0"
    lazy val jodaTime        = "joda-time" % "joda-time" % "2.2"
    lazy val jodaConvert     = "org.joda" % "joda-convert" % "1.3.1"
    lazy val jackson         = "org.codehaus.jackson" % "jackson-core-asl" % "1.9.10"
    lazy val jacksonMapper   = "org.codehaus.jackson" % "jackson-mapper-asl" % "1.9.10"
    lazy val jcip            = "net.jcip" % "jcip-annotations" % "1.0"
    lazy val nvI18n          = "com.neovisionaries" % "nv-i18n" % "1.4"

    lazy val scalatest       = "org.scalatest" % "scalatest_2.10.0" % "2.0.M5" % "test;it"
    lazy val logbackClassic  = "ch.qos.logback" % "logback-classic" % "1.0.13" % "it"
    lazy val logbackCore     = "ch.qos.logback" % "logback-core" % "1.0.13" % "it"
    lazy val playTest        = "play" % "play-test_2.10" % "2.1.1" % "it"
    lazy val play            = javaCore % "it"
  }

  // ----------------------
  // IDE specific
  // ----------------------
  override def settings = super.settings ++ Seq(
    //make sure "play eclipse" includes subprojects too
    EclipsePlugin.EclipseKeys.skipParents in ThisBuild := false
  )
}
