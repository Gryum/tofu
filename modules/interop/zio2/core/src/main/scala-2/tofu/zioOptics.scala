package tofu

import glass.PContains
import zio.{Tag, ZEnvironment}

object zioOptics {
  def hasContains[R, A: Tag, B: Tag]: PContains[ZEnvironment[R with A], ZEnvironment[R with A with B], A, B] =
    new PContains[ZEnvironment[R with A], ZEnvironment[R with A with B], A, B] {
      override def set(s: ZEnvironment[R with A], b: B): ZEnvironment[R with A with B] =
        s.add[B](b)

      override def extract(s: ZEnvironment[R with A]): A = s.get[A]
    }
}
