package chouc.scala.implic

import java.io.File

import scala.io.Source



class RichFile(file: File) {
  def read() = Source.fromFile(file).mkString
}

object implicitMethod {
  implicit def file2RichFile(file: File) : RichFile = {
    new RichFile(file)
  }
}

object RichFile {
  def main(args: Array[String]) {
    val contents1 = new RichFile(new File("c://words.txt")).read()
    import implicitMethod.file2RichFile
    val contents2 = new File("c://words.txt").read()
    println(contents1)
    println(contents2)
  }
}


