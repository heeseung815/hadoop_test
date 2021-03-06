import java.io.InputStream
import java.net.Socket

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}

import scala.concurrent.{Await, Future}
import scala.util.{Failure, Success}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

import scala.language.postfixOps

object Hdfs extends App {

  val conf = new Configuration()
  conf.set("fs.defaultFS", "hdfs://192.168.1.72:8020")
  System.setProperty("HADOOP_USER_NAME", "hadoop")
  //  conf.set("fs.defaultFS", "hdfs://localhost:9000")
  //  System.setProperty("HADOOP_USER_NAME", "hanyoungtak")

  def write(filePath: String, data: Array[Byte]) = {
    val path = new Path(filePath)
    val fs = FileSystem.get(conf)
    val os = fs.create(path)
    os.write(data)
    fs.close()
  }

  def removeFile(filename: String): Boolean = {
    val fs = FileSystem.get(conf)
    val path = new Path(filename)
    fs.delete(path, true)
  }

  def getFile(filename: String): InputStream = {
    val fs = FileSystem.get(conf)
    val path = new Path(filename)
    fs.open(path)
  }

  def createFolder(folderPath: String): Unit = {
    val path = new Path(folderPath)
    val fs = FileSystem.get(conf)
    if (!fs.exists(path)) {
      fs.mkdirs(path)
    }
  }

  def inputToFile(is: java.io.InputStream) {
    val in = scala.io.Source.fromInputStream(is)
    //val out = new java.io.PrintWriter(f)
    //try { in.getLines().foreach(out.println(_)) }
    try { in.getLines().foreach(println) }
    //finally { out.close }
  }

  Hdfs.write("/testFile1.txt", "Hello World".getBytes)
  Hdfs.write("/test1.txt", "Hello World".getBytes)
  Hdfs.inputToFile(Hdfs.getFile("/testFile1.txt"))
  Hdfs.removeFile("/test1.txt")
  Hdfs.createFolder("/testFolder1")
}