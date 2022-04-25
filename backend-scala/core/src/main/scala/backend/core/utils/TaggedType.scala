package backend.core.utils

//https://github.com/softwaremill/scala-common#tagging
//port from scalaz 7.2 before they changed the implementation in Scalaz 7.3
//this was the only part we needed
object TaggedType {
  private[tagging] type Tagged[A, T] = {type Tag = T; type Self = A}
  type @@[T, Tag] = Tagged[T, Tag]
  object Tag {
    @inline def apply[@specialized A, T](a: A): A @@ T = a.asInstanceOf[A @@ T]
    @inline def unwrap[@specialized A, T](a: A @@ T): A = a.asInstanceOf[A]
  }
}
