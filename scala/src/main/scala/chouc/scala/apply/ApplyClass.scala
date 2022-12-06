package chouc.scala.apply

class ApplyClass(val name: String) {
  private val list = List(1, 5, 2, 3, 6, 7)

  var age: Int = _

  def this(name: String, age: Int) = {
    this(name)
    this.age = age
  }

  def apply(): String = name

  def apply(index: Int): Int = list(index)

}

object ApplyClass {
  // 在 object对象上定义apply方法是用来创建对象的
  // 调用主构造器创建对象
  def apply(name: String): ApplyClass = new ApplyClass(name)

  // 调用辅助构造器创建对象
  // apply方法是可以重载的
  def apply(name: String, age: Int): ApplyClass = new ApplyClass(name, age)


  def main(args: Array[String]): Unit = {
    val applyClass = ApplyClass("name")
    val applyClass2 = ApplyClass("name", 1)

  }

}
