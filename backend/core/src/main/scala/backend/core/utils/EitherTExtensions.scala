package backend.core.utils

import cats.Applicative
import cats.data.EitherT

import scala.concurrent.ExecutionContext

trait EitherTExtensions {

  implicit class FromA[A](a: A) {
    def toEitherT[F[_], E](implicit executionContext: ExecutionContext, A: Applicative[F]): EitherT[F, E, A] =
      EitherT.rightT[F, E](a)
  }

  implicit class FromFA[F[_], A](f: F[A])(implicit A: Applicative[F]) {
    def toEitherT[E](implicit executionContext: ExecutionContext): EitherT[F, E, A] =
      EitherT.liftF(f)
  }

  implicit class FromEither[E, A](either: Either[E, A]) {
    def toEitherT[F[_]](implicit executionContext: ExecutionContext, A: Applicative[F]): EitherT[F, E, A] =
      EitherT.fromEither[F](either)
  }

  implicit class FromFEither[F[_], E, A](fEither: F[Either[E, A]])(implicit A: Applicative[F]) {
    def toEitherT: EitherT[F, E, A] =
      EitherT(fEither)
  }

  implicit class FromOption[E, A](option: Option[A]) {
    def toEitherT[F[_]](ifNone: E)
                       (implicit executionContext: ExecutionContext, A: Applicative[F]): EitherT[F, E, A] =
      EitherT.fromOption[F](option, ifNone)
  }

  implicit class FromFOption[F[_], E, A](fOption: F[Option[A]])(implicit A: Applicative[F]) {
    def toEitherT(ifNone: E)
                 (implicit executionContext: ExecutionContext): EitherT[F, E, A] =
      EitherT.fromOptionF(fOption, ifNone)
  }

}
