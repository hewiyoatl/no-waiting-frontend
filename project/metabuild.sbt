
  lazy val microservicePlugin =
      ProjectRef(uri(s"ssh://git@github.com/rocketlawyer/microservice-sbt-plugin.git#72190cb"), "microservice-sbt-plugin")

  lazy val build =
    project.in(file("."))
      .dependsOn(microservicePlugin)