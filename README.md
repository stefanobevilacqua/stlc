# STLC: Simply typed lambda calculus

Simple implementation of the [simply typed lambda calculus](https://en.wikipedia.org/wiki/Simply_typed_lambda_calculus), 
extended with Pair and Either types. This project contains just a parser and the 
type checker. It can be used as part of a larger project (an editor or a bigger
programming language), or to solve simple exercises of propositional logic via
the [Curry-Howard isomorphism](https://en.wikipedia.org/wiki/Curry%E2%80%93Howard_correspondence).

## Build
```shell
./mvnw clean compile
```

## Examples
This project doesn't contain an executable, examples of usage are in the tests, 
see [`StlcTest.kt`](src/test/kotlin/com/stefanobevilacqua/stlc/StlcTest.kt).