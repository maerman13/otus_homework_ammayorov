package module1

import java.util.UUID
import scala.annotation.tailrec
import java.time.Instant

import scala.language.postfixOps



/**
 * referential transparency
 */


 object referential_transparency{

  case class Abiturient(id: String, email: String, fio: String)

  type Html = String

  sealed trait Notification

  object Notification{
    case class Email(email: String, text: Html) extends Notification
    case class Sms(telephone: String, msg: String) extends Notification
  }


  case class AbiturientDTO(email: String, fio: String, password: String)

  trait NotificationService{
    def sendNotification(notification: Notification): Unit
    def createNotification(abiturient: Abiturient): Notification
  }


  trait AbiturientService{

    def registerAbiturient(abiturientDTO: AbiturientDTO): Abiturient
  }

  class AbiturientServiceImpl(val notificationService: NotificationService) extends AbiturientService{
    override def registerAbiturient(abiturientDTO: AbiturientDTO): Abiturient = {
      val notification = Notification.Email("", "")
      val abiturient = Abiturient(UUID.randomUUID().toString, abiturientDTO.email, abiturientDTO.fio)
      //notificationService.sendNotification(notification)
      // save(abiturient)
      abiturient
    }
  }


}


 // recursion

object recursion {

  /**
   * Реализовать метод вычисления n!
   * n! = 1 * 2 * ... n
   */

  def fact(n: Int): Int = {
    var _n = 1
    var i = 2
    while (i <= n){
      _n *= i
      i += 1
    }
    _n
  }


  def factRec(n: Int): Int = {
    if( n <= 0) 1 else n * factRec(n - 1)
  }

  def factTailRec(n: Int): Int = {

    def loop(n: Int, accum: Int): Int =
      if( n <= 1) accum
      else loop(n - 1, n * accum)
    loop(n, 1)
  }



  /**
   * реализовать вычисление N числа Фибоначчи
   * F0 = 0, F1 = 1, Fn = Fn-1 + Fn - 2
   *
   */


}

object hof{

   trait Consumer{
       def subscribe(topic: String): LazyList[Record]
   }

   case class Record(value: String)

   case class Request()
   
   object Request {
       def parse(str: String): Request = ???
   }

  /**
   *
   * Реализовать ф-цию, которая будет читать записи Request из топика,
   * и сохранять их в базу
   */
   def createRequestSubscription() = {
     val cons: Consumer = ???
     val stream: LazyList[Record] = cons.subscribe("requests")
     stream.foreach{ rec =>
       val request = Request.parse(rec.value)
       // saveToDB(request)
     }
   }

  def createSubscription[T](topic: String)(action: LazyList[Record] => T): T = {
    val cons: Consumer = ???
    val stream: LazyList[Record] = cons.subscribe(topic)
    action(stream)
  }

  val createRequestSubscription2 = createSubscription("requests"){ l =>
    l.foreach{ r =>
      val request = Request.parse(r.value)
      // saveToDB(request)
    }
  }
  

  // обертки

  def logRunningTime[A, B](f: A => B): A => B = a => {
    val start = System.currentTimeMillis()
    val result = f(a)
    val end = System.currentTimeMillis()
    println(end - start)
    result
  }

  def doomy(str: String): Unit = {
    Thread.sleep(1000)
    println(str)
  }

  val lDoomy: String => Unit = logRunningTime(doomy)

  lDoomy("Hello")




  // изменение поведения ф-ции

  val arr = Array(1, 2, 3, 4, 5)

  def isOdd(i: Int): Boolean = i % 2 > 0

  def not[A](f: A => Boolean): A => Boolean = a => !f(a)
  
  lazy val isEven: Int => Boolean = not(isOdd)
  isOdd(2) // boolean
  isEven(3) // boolean




  // изменение самой функции

  // Follow type implementation

  def partial[A, B, C](a: A, f: (A, B) => C): B => C =
    b => f(a, b)

  def sum(x: Int, y: Int): Int = x + y

  val p: Int => Int = partial(3, sum)

  p(3) // 6
  p(2) // 5

}


/**
 *  Реализуем тип Option
 */


 object opt {

  /**
   *
   * Реализовать тип Option, который будет указывать на присутствие либо отсутсвие результата
   */

  // Covariant - animal родитель dog, Option[Animal] родитель Option[Dog]
  // Contravariant - animal родитель dog, Option[Dog] родитель Option[Animal]
  // Invariant - нет отношений

  // Вопрос вариантности

  trait Option[+T]{

    def isEmpty: Boolean = this match {
      case Option.None => true
      case Option.Some(v) => false
    }

    def get: T = this match {
      case Option.Some(v) => v
      case Option.None => throw new Exception("get on empty Option")
    }

    def map[B](f: T => B): Option[B] =
      flatMap(v => Option(f(v)))

    def flatMap[B](f: T => Option[B]): Option[B] = this match {
      case Option.None => Option.None
      case Option.Some(v) => f(v)
    }

    /**
     *
     * Реализовать метод printIfAny, который будет печатать значение, если оно есть
     */

    def printIfAny: Unit = this match {
      case Option.Some(v) => println(v)
      case Option.None =>
    }

    /**
     *
     * Реализовать метод zip, который будет создавать Option от пары значений из 2-х Option
     */

    def zip[A1 >: T, B](option2: Option[B]): Option[(T, B)]  = {

      this match {
        case Option.Some(x) => {
          option2 match {
            case Option.Some(v) => Option.Some[(T, B)]((this.get, option2.get))
            case Option.None => Option.Some[(T, B)]((this.get, null.asInstanceOf[B]))
          }
        }
        case Option.None => {
          option2 match {
            case Option.Some(v) => Option.Some[(T, B)]((null.asInstanceOf[T], option2.get))
            case Option.None => Option.Some[(T, B)]((null.asInstanceOf[T], null.asInstanceOf[B]))
          }
        }
      }
    }

    /**
     *
     * Реализовать метод filter, который будет возвращать не пустой Option
     * в случае если исходный не пуст и предикат от значения = true
     */

    def filter(p: T => Boolean): Option[T] = {
      if (!this.isEmpty && p(this.get)) this else Option.None
    }

  }

  val a: Option[Int] = ???

  val r: Option[Int] = a.map(i => i + 1)

  object Option{

    final case class Some[T](v: T) extends Option[T]
    final case object None extends Option[Nothing]

    def apply[T](v: T): Option[T] = Some(v)
  }
 }



object list {
  /**
   *
   * Реализовать односвязанный иммутабельный список List
   * Список имеет два случая:
   * Nil - пустой список
   * Cons - непустой, содердит первый элемент (голову) и хвост (оставшийся список)
   */

  trait List[+T]{

    def head(): T = {
      this match {
        case List.::(x, y) => x
        case List.Nil => throw new Exception("head on empty list")
      }
    }

    def tail[TT >: T](): List[T] = {
      this match {
        case List.::(x, y) => y
        case List.Nil => throw new Exception("tail on empty list")
      }
    }

    def isEmpty[TT >: T](): Boolean = {
      this match {
        case List.::(x, y) => false
        case List.Nil => true
      }
    }

    /**
     * Метод cons, добавляет элемент в голову списка, для этого метода можно воспользоваться названием `::`
     *
     */

    def cons[TT >: T](elem: TT): List[TT] = {
      List.::(elem, this.asInstanceOf[List[TT]])
    }

    /**
     * Метод mkString возвращает строковое представление списка, с учетом переданного разделителя
     *
     */

    def mkString(delimeter: String): String = {
      var stringRepresentaion: String = ""
      mkStringFromHead(this)
      def mkStringFromHead(currentList: List[T]): Unit = currentList.isEmpty() match {
        case false => {
          stringRepresentaion += currentList.head()
          if (!currentList.tail().isEmpty()) stringRepresentaion += delimeter
          if (!currentList.tail().isEmpty()) mkStringFromHead(currentList.tail())
        }
        case true => throw new Exception("mkString on empty list")
      }
      stringRepresentaion
    }

    /**
     *
     * Реализовать метод reverse который позволит заменить порядок элементов в списке на противоположный
     */

    def reverse[TT >: T](): List[T] = {
      var reserseList = List[T]()
      createReverseFromHead(this)
      def createReverseFromHead(currentList: List[T]): Unit = currentList.isEmpty() match {
        case false => {
          reserseList = reserseList.cons(currentList.head())
          if (!currentList.tail().isEmpty()) createReverseFromHead(currentList)
        }
        case true => throw new Exception("reverse on empty list")
      }
      reserseList
    }

    /**
     *
     * Реализовать метод map для списка который будет применять некую ф-цию к элементам данного списка
     */

    def map[TT >: T](func: T => TT): List[T] = {
      var mapList = List[T]()
      def createMapFromHead(currentList: List[T]): Unit = currentList.isEmpty() match {
        case false => {
          mapList = mapList.cons(func(currentList.head()).asInstanceOf[T])
          if (!currentList.tail().isEmpty()) createMapFromHead(currentList)
        }
        case true => throw new Exception("map on empty list")
      }
      mapList.reverse()
    }

    /**
     *
     * Реализовать метод filter для списка который будет фильтровать список по некому условию
     */

    def filter[TT >: T](p: T => Boolean): List[T] = {
      var filtredList = List[T]()
      createFilterFromHead(this)
      def createFilterFromHead(currentList: List[T]): Unit = currentList.isEmpty() match {
        case false => {
          if(p(currentList.head())) {
            filtredList = filtredList.cons(currentList.head())
          }
          if (!currentList.tail().isEmpty()) createFilterFromHead(currentList.tail())
        }
        case true => throw new Exception("filter on empty list")
      }
      filtredList.reverse()
    }

    /**
     *
     * Написать функцию incList котрая будет принимать список Int и возвращать список,
     * где каждый элемент будет увеличен на 1
     */

    def incList(intList: List[Int]): List[Int] = {
      intList.map(x => x + 1)
    }

    /**
     *
     * Написать функцию shoutString котрая будет принимать список String и возвращать список,
     * где к каждому элементу будет добавлен префикс в виде '!'
     */

    def shoutString(stingList: List[String]) = {
      stingList.map(x => "!" + x)
    }
  }

  object List{
    case class ::[A](headElem: A, tailElem: List[A]) extends List[A]
    case object Nil extends List[Nothing]

    /**
     * Конструктор, позволяющий создать список из N - го числа аргументов
     * Для этого можно воспользоваться *
     *
     * Например вот этот метод принимает некую последовательность аргументов с типом Int и выводит их на печать
     * def printArgs(args: Int*) = args.foreach(println(_))
     */

    def apply[A](v: A*): List[A] = if(v.isEmpty) List.Nil
    else new ::(v.head, apply(v.tail:_*))
  }
}