package org.apache.spark.ui.dstream

import org.apache.spark.streaming.StreamingContext
import org.apache.spark.ui.{UIUtils, WebUIPage}

import javax.servlet.http.HttpServletRequest
import scala.xml.Node

class DStreamInfoPage(parent: StreamingAdditionTab) extends WebUIPage("") {


  override def render(request: HttpServletRequest): Seq[Node] = {
    //    val resources = generateLoadResources(request)

    val command = request.getParameter("command")
    println(s"******************** command: $command")
    execute(command)
    val content =
      <div>
        <H1>dstream</H1>
        <a href="./?command=stop">stop</a>
      </div>

    UIUtils.headerSparkPage(request, "mxb", Seq(content), parent, Option(0), Option(""), false, false)
  }

  def execute(commond: String): Unit = {
    commond match {
      case "stop" => {
        StreamingContext.getActive() match {
          case Some(ssc) => {
            println("************** Some")
            ssc.stop(true, true)
            println("************** stoped")
          }
          case None => {
            println("************** None")
          }
        }
      }
      case _ => {
        println(s"************** Sorry, not support this command $commond")
      }
    }

  }


}
