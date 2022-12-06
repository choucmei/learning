package chouc.scala.apply

class UnapplyClass(val name: String) {
  private val list = List(1, 5, 2, 3, 6, 7)

  var age: Int = _

  def this(name: String, age: Int) = {
    this(name)
    this.age = age
  }

}

object UnapplyClass {
  def apply(name: String): UnapplyClass = new UnapplyClass(name)

  def apply(name: String, age: Int): UnapplyClass = new UnapplyClass(name, age)

  def unapply(arg: UnapplyClass): Option[(String, Int)] = {
    Some((arg.name, arg.age))
  }

  def main(args: Array[String]): Unit = {
    val demo = new UnapplyClass("name", 1)
    val demo2 = new UnapplyClass("name")

    demo2 match {
      // type1: 由于是直接匹配对象，所以不需要走unapply方法
      //      case x : UnapplyDemo => println(s"type1==>${x.name}, ${x.age}")
      // 下面的都是提取对象里面数据的，这些都走unapply方法
      case UnapplyClass(x, y) => println(s"type2==>${x}, ${y}")
      case UnapplyClass(x) => println(s"type3==>${x._1}, ${x._2}")
      case UnapplyClass("hainiu", y) => println(s"type4==>hainiu, ${y}")
    }
  }
}
