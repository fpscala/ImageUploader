name := "supports"

lazy val support_services = project.in(file("services"))
lazy val support_logback = project.in(file("logback"))

aggregateProjects(
  support_services,
  support_logback,
)
