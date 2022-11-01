import requests.Response
import HelperUtils.{CreateLogger, ObtainConfigReference}
import clientLog.logger
import com.typesafe.config.Config
import org.slf4j.Logger

import collection.JavaConverters.*
import scala.concurrent.{Await, Future, duration}
import concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import concurrent.duration.DurationInt
import scala.util.{Failure, Success, Try}

object clientLog:
  val logger: Logger = CreateLogger(classOf[clientLog.type])

def main(args: Array[String]): Unit = {
  val config: Config = ObtainConfigReference("lambdaInvoke") match {
    case Some(value) => value
    case None => throw new RuntimeException("Unable to get configuration")
  }
  val lambdaConfig: Config = config.getConfig("lambdaInvoke")
  val URI = lambdaConfig.getString("lambdaAPI")

  logger.info("Invoking lambda API Gateway using timestamps given as arguments. They must be of HH:MM:SS.SSS format.")
  if(args.length==0){
    logger.error("No arguments given! Please run again")
  }
  logger.info("Lower: " + args(0) + " " + "Upper: " + args(1))
  val r: Response = requests.get(URI
  , params = Map("lower" -> args(0), "upper" -> args(1)))
  val responseText = r.text()
  logger.info("Lambda return: " + responseText)


}