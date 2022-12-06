package chouc.scala.apply

class UnapplyDemo(val name: String, val age: Int) {
}

object UnapplyDemo {
  // unapply方法是用来解构的
  // 你给我对象，我把对象里的数据提取出来(通过模式匹配)
  def unapply(arg: UnapplyDemo): Option[(String, Int)] = {
    println("==> unapply(arg: UnapplyDemo): Option[(String, Int)]")
    Some((arg.name, arg.age))
  }

  def main(args: Array[String]): Unit = {
    val demo = new UnapplyDemo("hainiu", 10)
    demo match {
      // type1: 由于是直接匹配对象，所以不需要走unapply方法
      //      case x : UnapplyDemo => println(s"type1==>${x.name}, ${x.age}")
      // 下面的都是提取对象里面数据的，这些都走unapply方法
      case UnapplyDemo(x, y) => println(s"type2==>${x}, ${y}")
      case UnapplyDemo(x) => println(s"type3==>${x._1}, ${x._2}")
      case UnapplyDemo("hainiu", y) => println(s"type4==>hainiu, ${y}")
    }
  }
}