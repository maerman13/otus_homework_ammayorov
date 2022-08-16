package futures

import com.sun.xml.internal.fastinfoset.sax.Features

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.util.{Failure, Success, Try}
import scala.concurrent.duration.Duration

object task_futures_sequence extends App {

  /**
   * В данном задании Вам предлагается реализовать функцию fullSequence,
   * похожую на Future.sequence, но в отличии от нее,
   * возвращающую все успешные и не успешные результаты.
   * Возвращаемое тип функции - кортеж из двух списков,
   * в левом хранятся результаты успешных выполнений,
   * в правово результаты неуспешных выполнений.
   * Не допускается использование методов объекта Await и мутабельных переменных var
   */

  /**
   * @param futures список асинхронных задач
   * @return асинхронную задачу с кортежом из двух списков
   */

  def fullSequence[A](futures: List[Future[A]])(implicit ex: ExecutionContext): Future[(List[Any], List[Throwable])] = {

    Future.sequence(futures.map(x => x.recover { case exception => exception }))
      .map(x =>
        (x.filter(z => !z.isInstanceOf[Throwable]).asInstanceOf[List[A]],
          x.filter(z => z.isInstanceOf[Throwable]).asInstanceOf[List[Throwable]]))
  }
}