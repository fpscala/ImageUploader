package uz.scala.setup

import uz.scala.aws.s3.AWSConfig
import uz.scala.http4s.HttpServerConfig

case class Config(
    http: HttpServerConfig,
    awsConfig: AWSConfig,
  )
