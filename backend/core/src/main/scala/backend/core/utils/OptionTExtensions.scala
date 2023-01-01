package backend.core.utils

import cats.Applicative
import cats.data.OptionT
import cats.implicits.{catsSyntaxOptionId, toFunctorOps}

import scala.concurrent.ExecutionContext

trait OptionTExtensions {

  implicit class OptionTFromA[A](a: A) {
    def toOptionT[F[_]](implicit executionContext: ExecutionContext, A: Applicative[F]): OptionT[F, A] =
      OptionT.fromOption[F](Some(a))
  }

  implicit class OptionTFromFA[F[_], A](f: F[A])(implicit A: Applicative[F]) {
    def toOptionT(implicit executionContext: ExecutionContext): OptionT[F, A] =
      OptionT.liftF(f)
  }

  implicit class OptionTFromOption[A](option: Option[A]) {
    def toOptionT[F[_]](implicit executionContext: ExecutionContext, A: Applicative[F]): OptionT[F, A] =
      OptionT.fromOption[F](option)
  }

  implicit class OptionTFromFOption[F[_], A](fOption: F[Option[A]])(implicit A: Applicative[F]) {
    def toOptionT: OptionT[F, A] =
      OptionT(fOption)
  }

  implicit class OptionTFromEither[E, A](either: Either[E, A]) {
    def toOptionT[F[_]](ifLeft: Option[A])
                 (implicit executionContext: ExecutionContext, A: Applicative[F]): OptionT[F, A] =
      OptionT.fromOption[F](either.map(_.some).getOrElse(ifLeft))
  }

  implicit class OptionTFromFEither[F[_], E, A](fEither: F[Either[E, A]])(implicit A: Applicative[F]) {
    def toOptionT(ifLeft: Option[A])
                 (implicit executionContext: ExecutionContext): OptionT[F, A] =
      OptionT(fEither.map(_.map(_.some).getOrElse(ifLeft)))
  }

}

