package chouc.scala.generic

class Pair[T <% Comparable[T]](val first: T, val second: T){
  def bigger = if(first.compareTo(second) > 0) first else second
}


object UpperBound {
  def main(args: Array[String]) {
    val p = new Pair(1, 5)
    println(p.bigger)
  }
}




