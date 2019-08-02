package chouc.scala.implic

//所有的隐式值和隐式方法必须放到object
object implicitVarable {
  implicit val aaaaa = "laozhao"
  implicit val i = 1
}

object ImplicitValue {


  def sayHi()(implicit name: String = "laoduan"): Unit = {
    println(s"hi~ $name")
  }

  def main(args: Array[String]) {

    println(1 to 10)

    sayHi()

    import implicitVarable.aaaaa
    sayHi()
  }

}
