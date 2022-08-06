package module1

import scala.util.Random

class Experiment {

  // 0 - черный шарик
  // 1 - белый шарий
  var urn: List[Int] = List(0, 0, 0, 1, 1, 1)

  def IstakeFirstBallBlack: Boolean = {
    val firstBallPosition = Random.nextInt(urn.size)
    val firstBall = urn(firstBallPosition)
    urn = urn.patch(firstBallPosition, Nil, 1)
    if (firstBall == 0) {
      true
    } else {
      false
    }
  }

  def IstakeWhiteBallWhite: Boolean = {
    val secondBall = urn(Random.nextInt(urn.size))
    // удаляем из урны шар, который был получен из неё
    if (secondBall == 1) {
      true
    } else {
      false
    }
  }
}


object main2 extends App {

  // определяем размер выборки
  val sampleSize = 10000

  // создаем массив экземлпяров экспериментов
  var experiment: Array[Experiment] = Array.fill[Experiment](sampleSize)(new Experiment())

  // достаем первый шарик и оставляем только те, где он выпал черный
  val experiment1 = experiment.filter(x => x.IstakeFirstBallBlack)

  //достаем второй шарик и оставляем только те, где он выпал белый
  val experiment2 = experiment1.filter(x => x.IstakeWhiteBallWhite)

  // получаем результирующую вероятность
  val result: Float = experiment2.size.toFloat / experiment1.size.toFloat

  println(result)
}