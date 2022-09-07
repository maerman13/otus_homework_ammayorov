package futures

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Success, Failure}

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
    futures.foldLeft(Future.successful((List[Any](), List[Throwable]()))) { (futures, future) =>
      futures.flatMap { case (accumSuccess, accumErrors) =>
        future
          .map { success => (accumSuccess :+ success, accumErrors) }
          .recover { case error: Throwable => (accumSuccess, accumErrors :+ error) }
      }
    }
  }
}