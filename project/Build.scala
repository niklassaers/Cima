import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "Cima"
    val appVersion      = "1.0"

    val appDependencies = Seq(
      // Add your project dependencies here,
      "net.databinder" %% "dispatch-http" % "0.8.8" withSources,
      "com.google.code.facebookapi" % "facebook-java-api" % "3.0.2" withSources

    )

    val main = PlayProject(appName, appVersion, appDependencies, mainLang = SCALA).settings(
      resolvers ++= Seq(
        "sbt-idea-repo" at "http://mpeltonen.github.com/maven/"
      )
    )

}
