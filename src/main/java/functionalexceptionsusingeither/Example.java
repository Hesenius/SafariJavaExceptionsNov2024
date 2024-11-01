package functionalexceptionsusingeither;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;
import java.util.function.Consumer;

class Either<S, F> {
  private S success;
  private F failure;

  private Either(S s, F f) {
    this.success = s;
    this.failure = f;
  }

  public static <S, F> Either<S, F> success(S success) {
    return new Either<>(success, null);
  }

  public static <S, F> Either<S, F> failure(F failure) {
    return new Either<>(null, failure);
  }

  public boolean isSuccess() {
    return failure == null;
  }

  public boolean isFailure() {
    return failure != null;
  }

  public S getSuccess() {
    if (isFailure()) throw new IllegalStateException("Getting success from failure");
    return success;
  }

  public F getFailure() {
    if (isSuccess()) throw new IllegalStateException("Getting failure from success");
    return failure;
  }

  public <S1, F1> Either<S1, F1> map(Function<Either<S, F>, Either<S1, F1>> op) {
    return op.apply(this);
  }

  public Either<S, F> report(Consumer<F> op) {
    if (isFailure()) {
      op.accept(failure);
    }
    return this;
  }

  public Either<S, F> recover(UnaryOperator<Either<S, F>> op) {
    if (isFailure()) {
      return op.apply(this);
    } else {
      return this;
    }
  }
}

@FunctionalInterface
interface ExFunction<A, R> {
  R apply(A a) throws Throwable;

  public static <A, R> Function<A, Either<R, Throwable>> wrap(ExFunction<A, R> op) {
    return a -> {
      try {
        return Either.success(op.apply(a));
      } catch (Throwable t) {
        return Either.failure(t);
      }
    };
  }
}

public class Example {
  public static void main(String[] args) {
    Function<String, Either<Stream<String>, Throwable>> getLines =
        ExFunction.wrap(fn -> Files.lines(Path.of(fn)));

    UnaryOperator<Either<Stream<String>, Throwable>> delay =
        e -> {
          try {
            Thread.sleep(1_000);
          } catch (InterruptedException ie) {
            System.out.println("Interrupted!");
          }
          return e;
        };

    UnaryOperator<Either<Stream<String>, Throwable>> retry =
        e -> getLines.apply(e.getFailure().getMessage());

    Map<String, String> fallbacks = Map.of("b.txt", "d.txt");
    // call from recover, no need to check if failure here
    UnaryOperator<Either<Stream<String>, Throwable>> fallbackFile =
        e -> getLines.apply(fallbacks.get(e.getFailure().getMessage()));

    Stream.of("a.txt", "b.txt", "c.txt")
        .map(ExFunction.wrap(fn -> Files.lines(Path.of(fn))))
        .map(e -> e.report(t -> System.out.println("Got a problem:" + t.getMessage())))
        .map(e -> e.recover(delay))
        .map(e -> e.recover(retry))
        .map(e -> e.recover(fallbackFile))
        .filter(e -> e.isSuccess())

        .flatMap(e -> e.getSuccess())
        .forEach(s -> System.out.println(s));
  }
}
