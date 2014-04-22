import sbt._
import sbt.Configuration
import sbt.Keys._
import play.Project._
import com.typesafe.sbteclipse.core.EclipsePlugin
import Release._

import de.johoop.jacoco4sbt._
import JacocoPlugin._
import sbtunidoc.Plugin._

object PlaySDKBuild extends Build {

  lazy val sdk = play.Project("sphere-sdk").
                  settings(standardSettings:_*).
                  settings(javadocSettings:_*).
                  settings(javaUnidocSettings:_*).
                  settings(libraryDependencies += Libs.junitDep).
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
    settings(playPlugin := true).
    settings(scalaSettings:_*).
    settings(java6Settings:_*).
    settings(testSettings(Libs.scalaTest, Libs.playTest, Libs.play):_*).
    configs(IntegrationTest).
    settings(
      scalaSource in IntegrationTest <<= baseDirectory (_ / "it"),
      unmanagedResourceDirectories in IntegrationTest <<= baseDirectory (base => Seq(base / "it" / "resources"))
    )

  // ----------------------
  // Java client
  // ----------------------

  lazy val sphereJavaClient = Project(
    id = "sphere-java-client",
    base = file("java-client"),
    settings =
      Defaults.defaultSettings ++ standardSettings ++ scalaSettings ++ java6Settings ++ Defaults.itSettings ++
        testSettings(Libs.scalaTest, Libs.logbackClassic, Libs.logbackCore, Libs.junitDep) ++ Seq(
        autoScalaLibrary := false, // no dependency on Scala standard library (just for tests)
        crossPaths := false,
        libraryDependencies ++= Seq(
          Libs.asyncHttpClient, Libs.guava, Libs.jodaTime, Libs.jodaConvert,
          Libs.jackson, Libs.jacksonMapper, Libs.jcip, Libs.typesafeConfig,
          Libs.nvI18n        // CountryCode
        ),
        sourceGenerators in Compile <+= (sourceManaged in Compile, version) map { (outDir, v) =>
          val file = outDir / "io" / "sphere" / "internal" / "Version.java"
          IO.write(file, """
package io.sphere.internal;

/** Current version of the Sphere Java client, useful for User Agent HTTP header, logging, etc. */
// Don't edit this file - it is autogenerated by sbt
public final class Version {
    public static final String version = """" + v + """";
}
""")
          Seq(file)
        }
      )).configs(IntegrationTest)

  // ----------------------
  // Settings
  // ----------------------

  val JavaDoc = config("genjavadoc") extend Compile

  //from https://github.com/typesafehub/genjavadoc
  lazy val javadocSettings = inConfig(JavaDoc)(Defaults.configSettings) ++ Seq(
    libraryDependencies += compilerPlugin("com.typesafe.genjavadoc" %%
      "genjavadoc-plugin" % "0.7" cross CrossVersion.full),
    scalacOptions <+= target map (t => "-P:genjavadoc:out=" + (t / "java")),
    packageDoc in Compile <<= packageDoc in JavaDoc,
    sources in JavaDoc <<=
      (target, compile in Compile, sources in Compile) map ((t, c, s) =>
        (t / "java" ** "*.java").get ++ s.filter(_.getName.endsWith(".java"))),
    javacOptions in JavaDoc := Seq(),
    artifactName in packageDoc in JavaDoc :=
      ((sv, mod, art) =>
        "" + mod.name + "_" + sv.binary + "-" + mod.revision + "-javadoc.jar")
  )

  val Snapshot = "SNAPSHOT"
  def isOnJenkins() = scala.util.Properties.envOrNone("JENKINS_URL").isDefined

  lazy val standardSettings = publishSettings ++ genjavadocSettings ++ Seq(
    organization := "io.sphere",
    version <<= version in ThisBuild,
    licenses := Seq("Apache" -> url("http://www.apache.org/licenses/LICENSE-2.0")),
    homepage := Some(url("https://github.com/commercetools/sphere-play-sdk")),
    version in ThisBuild <<= (version in ThisBuild) { v =>
      //Jenkins is supposed to publish every snapshot artifact which can be distinguished per Git commit.
      if (isOnJenkins) {
        val shortenedGitHash = Process("git rev-parse HEAD").lines.head.substring(0, 7)
        if(v.endsWith(Snapshot) && !v.contains(shortenedGitHash)){
          v.replace(Snapshot, "") + shortenedGitHash + "-" + Snapshot
        }else {
          v
        }
      } else {
        v
      }
    }
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
         testOptions in testScope <<= (target in testScope) map { targetDir => Seq(
           //Tests.Argument(TestFrameworks.ScalaTest, "-l", "disabled integration"),
           Tests.Argument(TestFrameworks.ScalaTest, "-oD"), // show durations
           Tests.Argument(TestFrameworks.ScalaTest, "-u", (targetDir / "test-reports").getCanonicalPath))
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
    lazy val typesafeConfig  = "com.typesafe" % "config" % "1.2.0"

    lazy val scalaTest       = "org.scalatest" %% "scalatest" % "2.0" % "test;it"
    lazy val logbackClassic  = "ch.qos.logback" % "logback-classic" % "1.0.13" % "it"
    lazy val logbackCore     = "ch.qos.logback" % "logback-core" % "1.0.13" % "it"
    lazy val junitDep        = "junit" % "junit-dep" % "4.11" % "test"
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
