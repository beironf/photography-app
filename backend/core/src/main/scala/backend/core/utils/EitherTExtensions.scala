package backend.core.utils

import cats.data.EitherT

import scala.concurrent.{ExecutionContext, Future}

trait EitherTExtensions {

  implicit class FromA[A](a: A) {
    def toEitherT[E](implicit executionContext: ExecutionContext): EitherT[Future, E, A] =
      EitherT.rightT[Future, E](a)
  }

  implicit class FromFutureA[A](future: Future[A]) {
    def toEitherT[E](implicit executionContext: ExecutionContext): EitherT[Future, E, A] =
      EitherT.liftF(future)
  }

  implicit class FromEither[E, A](either: Either[E, A]) {
    def toEitherT(implicit executionContext: ExecutionContext): EitherT[Future, E, A] =
      EitherT.fromEither[Future](either)
  }

  implicit class FromFutureEither[E, A](futureEither: Future[Either[E, A]]) {
    def toEitherT: EitherT[Future, E, A] =
      EitherT(futureEither)
  }

  implicit class FromOption[E, A](option: Option[A]) {
    def toEitherT(ifNone: E)
                 (implicit executionContext: ExecutionContext): EitherT[Future, E, A] =
      EitherT.fromOption[Future](option, ifNone)
  }

  implicit class FromFutureOption[E, A](futureOption: Future[Option[A]]) {
    def toEitherT(ifNone: E)
                 (implicit executionContext: ExecutionContext): EitherT[Future, E, A] =
      EitherT.fromOptionF(futureOption, ifNone)
  }

}
