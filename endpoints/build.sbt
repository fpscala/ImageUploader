name := "endpoints"

lazy val `endpoints-domain` = project
  .in(file("00-domain"))
  .dependsOn(
    LocalProject("common"),
    LocalProject("support_services"),
  )

lazy val `endpoints-core` =
  project
    .in(file("02-core"))
    .dependsOn(
      `endpoints-domain`,
      LocalProject("integration_aws-s3"),
    )

lazy val `endpoints-api` =
  project
    .in(file("03-api"))
    .dependsOn(
      `endpoints-core`
    )

lazy val `endpoints-server` =
  project
    .in(file("04-server"))
    .dependsOn(`endpoints-api`)

lazy val `endpoints-runner` =
  project
    .in(file("05-runner"))
    .dependsOn(
      `endpoints-server`
    )
    .settings(DockerImagePlugin.serviceSetting("endpoints"))
    .enablePlugins(DockerImagePlugin, JavaAppPackaging, DockerPlugin)

aggregateProjects(
  `endpoints-domain`,
  `endpoints-core`,
  `endpoints-api`,
  `endpoints-server`,
  `endpoints-runner`,
)
