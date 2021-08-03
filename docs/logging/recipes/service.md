---
id: tofu.logging.recipes.service
title: Tofu Logging recipes
---


## Service logging
One downside of a [simple logging](simple.md) is that in every service class you have to write this line:
```scala
private implicit val logging: Logging[F] = Logging.Make[F].forService[MyService[F]]
```
It is quite boilerplate-ish, so `tofu.logging` has the workaround:

```scala
class MyService[F[_]: Monad: MyService.Log](someDependency: DependencyService){
  
  def makeThis: F[Unit] = someDependency.foo(30) >> info"Something"
  
  def makeThat: F[Unit] = someDependency.foo(30).flatTap(result => warn"Some another thing $result")
}

object MyService extends LoggingCompanion[MyService]
```

The line `object MyService extends LoggingCompanion[MyService]` mixes into the companion object type Log, 
which is just alias for [`ServiceLogging[F, MyService]`](../main-entities.md#logging).

This type carries information about which class it is supposed to be used for, 
so Scala compiler is able to implicitly create from Logs and pass desired `ServiceLogging` instance.

And in the wiring of the app, just as in [simple logging](simple.md):

```scala
import cats.effect.ExitCode
def run: IO[ExitCode] = {
  implicit val logs: Logging.Make[IO] = Logging.Make.plain[IO] // or Make.contextual[IO, C]
  
  val service = new MyService[IO](???)
}
```
The `MyService.Log` instance is created implicitly just for this service from the implicit value of `Logging.Make`.