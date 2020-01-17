package chouc.scala.cases

object WordCount {
  def main(args: Array[String]): Unit = {
    val list = Seq("hello aasd asdff asdfff ff ff","asdf ook qwecfs sdf wex dfsdf"," aa fasdf asdfas fasdfasdf").toList
    list.flatMap(_.split(" ")).map((_,1)).groupBy(_._1).foldLeft(_+_)
  }
}
