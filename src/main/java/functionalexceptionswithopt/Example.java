package functionalexceptionswithopt;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

@FunctionalInterface
interface ExFunction<A, R> {
  R apply(A a) throws Throwable;

  static <A, R> Function<A, Optional<R>> wrap(ExFunction<A, R> op) {
    return a -> {
      try {
        return Optional.of(op.apply(a));
      } catch (Throwable t) {
        return Optional.empty();
      }
    };
  }
}

// Data type wrapping "valid result" or not
// "Optional<E>"

public class Example {
  public static Optional<Stream<String>> getLines(String fn) {
    try {
      return Optional.of(Files.lines(Path.of(fn)));
    } catch (Throwable t) {
      return Optional.empty();
    }
//  public static Stream<String> getLines(String fn) {
//    try {
//      return Files.lines(Path.of(fn));
//    } catch (Throwable t) {
//      System.err.println("problem opening " + fn);
////      throw new RuntimeException(t);
//      return Stream.empty();
//    }
  }

  public static void reportProblem(Optional<Stream<String>> oss) {
    if (oss.isEmpty()) {
      System.out.println("There was a problem opening a file");
    }
  }
  public static void main(String[] args) {

    ExFunction<String, Stream<String>> getTheLines = fn -> Files.lines(Path.of(fn));

    Stream.of("a.txt", "b.txt", "c.txt")
//        .flatMap(fn -> Files.lines(Path.of(fn)))
//        .flatMap(fn -> getLines(fn))
//        .map(fn -> getLines(fn))
        .map(ExFunction.wrap(fn -> Files.lines(Path.of(fn))))
        .peek(opt -> reportProblem(opt))
        .filter(opt -> opt.isPresent())
        .flatMap(opt -> opt.get())
        .forEach(s -> System.out.println(s));
  }
}
