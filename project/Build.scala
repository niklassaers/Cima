import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "Play20WithNeo4J"
    val appVersion      = "1.0"

    var facebookKey     = "209718142399215"
    var facebookSecret  = "7675c9ecc3b9b2c15003fa43e92035fe"

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
