package chouc.scala

/**
  * @Title: WordCount
  * @Package chouc.scala
  * @Description:
  * @author chouc
  * @date 10/14/19
  * @version V1.0
  */
object WordCount {
  def main(args: Array[String]): Unit = {
    val a = List(1, 7, 2, 9).reduceLeft(_ + _)

    val s = Seq("asd asdf fff aaa", "ffa fs aaa", "ffff xax")

    //
    s.flatMap(_.split(" ")).map((_, 1)).groupBy(_._1).map(x => (x._1, x._2.size)).foreach(println(_))
    s.flatMap(_.split(" ")).map((_, 1)).groupBy(_._1).mapValues(_.size).toList.sortBy(_._2).take(3);
    s.flatMap(_.split(" ")).map((_, 1)).groupBy(_._1).mapValues(_.foldLeft(0)((a, b) => a + b._2)).foreach(println(_))
  }
}
