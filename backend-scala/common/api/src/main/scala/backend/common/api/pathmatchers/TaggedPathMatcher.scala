package backend.common.api.pathmatchers

import akka.http.scaladsl.server.PathMatcher1
import scalaz.{@@, Tag}

object TaggedPathMatcher {
  implicit class TransformedSegment[SEG](segment: PathMatcher1[SEG]) {
    def as[TAGGED](implicit asTaggedMapper: SEG => TAGGED): PathMatcher1[TAGGED] = segment.map(asTaggedMapper)
  }

  implicit def taggedPathId[TAGGED, SEG]: SEG => SEG @@ TAGGED = (value: SEG) => Tag[SEG, TAGGED](value)
}
