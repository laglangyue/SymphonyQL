import sbt.*

object Dependencies {

  object Versions {
    val scala3_Version       = "3.3.1"
    val `pekko-core_Version` = "1.0.2"
    val `pekko-http_Version` = "1.0.0"
    val `parboiled_Version`  = "2.5.1"
    val `scalatest_Version`  = "3.2.17"
  }

  object Deps {
    import Versions.*

    // parser schema and query, convert to ADTs, types definition
    val parser = sbt.Def.setting {
      Seq(
        "org.parboiled" %% "parboiled" % `parboiled_Version`,
        "org.scalatest" %% "scalatest" % `scalatest_Version` % Test
      )
    }

    // prepare, analyzer, run, tracing
    val core = sbt.Def.setting {
      Seq(
        "org.apache.pekko" %% "pekko-stream"  % `pekko-core_Version`,
        "org.apache.pekko" %% "pekko-testkit" % `pekko-core_Version` % Test
      )
    }

    // default http server
    val server = sbt.Def.setting {
      Seq(
        "org.apache.pekko" %% "pekko-http" % `pekko-http_Version`
      )
    }
  }

}
