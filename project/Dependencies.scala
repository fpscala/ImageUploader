import sbt.*

object Dependencies {
  object Versions {
    lazy val circe = "0.14.1"
    lazy val http4s = "0.23.23"
    lazy val refined = "0.10.2"
    lazy val cats = "2.12.0"
    lazy val `cats-effect` = "3.5.4"
    lazy val logback = "1.5.6"
    lazy val log4cats = "2.7.0"
    lazy val pureconfig = "0.17.2"
    lazy val fs2 = "3.6.1"
    lazy val enumeratum = "1.7.3"
    lazy val awsSdk = "1.12.661"
    lazy val awsSoftwareS3 = "2.25.27"
  }
  trait LibGroup {
    def all: Seq[ModuleID]
  }
  object com {
    object amazonaws extends LibGroup {
      private def awsJdk(artifact: String): ModuleID =
        "com.amazonaws" % artifact % Versions.awsSdk

      lazy val awsCore: ModuleID = awsJdk("aws-java-sdk-core")
      lazy val awsS3: ModuleID = awsJdk("aws-java-sdk-s3")
      val awsSoftwareS3: ModuleID = "software.amazon.awssdk" % "s3" % Versions.awsSoftwareS3

      override def all: Seq[ModuleID] = Seq(awsCore, awsS3, awsSoftwareS3)
    }
    object github {
      object pureconfig extends LibGroup {
        private def repo(artifact: String): ModuleID =
          "com.github.pureconfig" %% artifact % Versions.pureconfig

        lazy val core: ModuleID = repo("pureconfig")
        lazy val enumeratum: ModuleID = repo("pureconfig-enumeratum")

        override def all: Seq[ModuleID] = Seq(core, enumeratum)
      }
    }
    object beachape {
      object enumeratum extends LibGroup {
        private def enumeratum(artifact: String): ModuleID =
          "com.beachape" %% artifact % Versions.enumeratum

        lazy val core: ModuleID = enumeratum("enumeratum")
        lazy val circe: ModuleID = enumeratum("enumeratum-circe")
        lazy val cats: ModuleID = enumeratum("enumeratum-cats")
        override def all: Seq[ModuleID] = Seq(core, circe, cats)
      }
    }
  }

  object io {
    object circe extends LibGroup {
      private def circe(artifact: String): ModuleID =
        "io.circe" %% s"circe-$artifact" % Versions.circe

      lazy val core: ModuleID = circe("core")
      lazy val generic: ModuleID = circe("generic")
      lazy val parser: ModuleID = circe("parser")
      lazy val refined: ModuleID = circe("refined")
      lazy val optics: ModuleID = circe("optics")
      lazy val `generic-extras`: ModuleID = circe("generic-extras")
      override def all: Seq[ModuleID] =
        Seq(core, generic, parser, refined, optics, `generic-extras`)
    }
  }
  object org {

    object typelevel {
      object cats {
        lazy val core = "org.typelevel"    %% "cats-core"           % Versions.cats
        lazy val effect = "org.typelevel"  %% "cats-effect"         % Versions.`cats-effect`
      }
      lazy val log4cats = "org.typelevel" %% "log4cats-slf4j" % Versions.log4cats
    }

    object http4s extends LibGroup {
      private def http4s(artifact: String): ModuleID =
        "org.http4s" %% s"http4s-$artifact" % Versions.http4s

      lazy val dsl = http4s("dsl")
      lazy val server = http4s("ember-server")
      lazy val client = http4s("ember-client")
      lazy val circe = http4s("circe")
      lazy val `blaze-server` = http4s("blaze-server")
      override def all: Seq[ModuleID] = Seq(dsl, server, client, circe)
    }
  }
  object eu {
    object timepit {
      object refined extends LibGroup {
        private def refined(artifact: String): ModuleID =
          "eu.timepit" %% artifact % Versions.refined

        lazy val core = refined("refined")
        lazy val cats = refined("refined-cats")
        lazy val pureconfig: ModuleID = refined("refined-pureconfig")

        override def all: Seq[ModuleID] = Seq(core, cats, pureconfig)
      }
    }
  }

  object ch {
    object qos {
      lazy val logback = "ch.qos.logback" % "logback-classic" % Versions.logback
    }
  }

  object co {
    object fs2 extends LibGroup {
      private def fs2(artifact: String): ModuleID =
        "co.fs2" %% s"fs2-$artifact" % Versions.fs2

      lazy val core: ModuleID = fs2("core")
      lazy val io: ModuleID = fs2("io")
      override def all: Seq[ModuleID] = Seq(core, io)
    }
  }

  object uz {
    object scala extends LibGroup {
      lazy val common: ModuleID = "uz.scala" %% "common" % "1.0.2"
      override def all: Seq[ModuleID] = Seq(common)
    }
  }
}
