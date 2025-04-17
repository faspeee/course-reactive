package com.example.stream.spring.courses.reactive.example.functional;

import java.util.Optional;
import java.util.function.Function;

/**
 * A generic container that holds either a left value of type {@code L} or a right value of type {@code R}, but not both.
 * <p>
 * Inspired by functional programming paradigms, {@code Either} is commonly used to represent a value that is one of two possibilities:
 * typically a success (Right) or a failure (Left).
 * </p>
 *
 * @param <L> the type of the Left value (often used to represent an error or exceptional case)
 * @param <R> the type of the Right value (often used to represent a success or valid case)
 */
public abstract class Either<L, R> {

    private Either() {
    }

    /**
     * Creates a new {@code Either} instance representing a Left value.
     *
     * @param value the Left value to store
     * @param <L>   the type of the Left value
     * @param <R>   the type of the Right value
     * @return a new {@code Left} instance
     */
    public static <L, R> Either<L, R> left(L value) {
        return new Left<>(value);
    }

    /**
     * Creates a new {@code Either} instance representing a Right value.
     *
     * @param value the Right value to store
     * @param <L>   the type of the Left value
     * @param <R>   the type of the Right value
     * @return a new {@code Right} instance
     */
    public static <L, R> Either<L, R> right(R value) {
        return new Right<>(value);
    }

    /**
     * Returns {@code true} if this Either instance is a {@code Left}.
     *
     * @return {@code true} if this is a Left, otherwise {@code false}
     */
    public abstract boolean isLeft();

    /**
     * Returns {@code true} if this Either instance is a {@code Right}.
     *
     * @return {@code true} if this is a Right, otherwise {@code false}
     */
    public abstract boolean isRight();

    /**
     * Retrieves the value if this is a {@code Left}, or returns {@code Optional.empty()} if this is a {@code Right}.
     *
     * @return an {@code Optional} containing the Left value, or empty
     */
    public abstract Optional<L> getLeft();

    /**
     * Retrieves the value if this is a {@code Right}, or returns {@code Optional.empty()} if this is a {@code Left}.
     *
     * @return an {@code Optional} containing the Right value, or empty
     */
    public abstract Optional<R> getRight();

    /**
     * Applies a mapping function to the Right value if present, otherwise returns the current Left.
     *
     * @param mapper a function to transform the Right value
     * @param <T>    the type of the new Right value
     * @return a new {@code Either} containing the mapped Right value, or the original Left
     */
    public abstract <T> Either<L, T> map(Function<? super R, ? extends T> mapper);

    /**
     * Applies a mapping function that returns another {@code Either} to the Right value if present,
     * otherwise returns the current Left.
     *
     * @param mapper a function to transform the Right value into another {@code Either}
     * @param <T>    the type of the new Right value
     * @return the result of the mapping function if this is a Right, or the original Left
     */
    public abstract <T> Either<L, T> flatMap(Function<? super R, Either<L, T>> mapper);

    /**
     * Applies one of two functions to the contained value depending on whether it is a Left or Right.
     *
     * @param leftMapper  function to apply if this is a Left
     * @param rightMapper function to apply if this is a Right
     * @param <T>         the type of the returned result
     * @return the result of applying the appropriate function
     */
    public abstract <T> T fold(Function<? super L, T> leftMapper, Function<? super R, T> rightMapper);

    /**
     * Represents the Left variant of the Either.
     *
     * @param <L> the type of the Left value
     * @param <R> the type of the Right value
     */
    private static final class Left<L, R> extends Either<L, R> {
        private final L value;

        private Left(L value) {
            this.value = value;
        }

        @Override
        public boolean isLeft() {
            return true;
        }

        @Override
        public boolean isRight() {
            return false;
        }

        @Override
        public Optional<L> getLeft() {
            return Optional.of(value);
        }

        @Override
        public Optional<R> getRight() {
            return Optional.empty();
        }

        @Override
        public <T> Either<L, T> map(Function<? super R, ? extends T> mapper) {
            return Either.left(value);
        }

        @Override
        public <T> Either<L, T> flatMap(Function<? super R, Either<L, T>> mapper) {
            return Either.left(value);
        }

        @Override
        public <T> T fold(Function<? super L, T> leftMapper, Function<? super R, T> rightMapper) {
            return leftMapper.apply(value);
        }
    }

    /**
     * Represents the Right variant of the Either.
     *
     * @param <L> the type of the Left value
     * @param <R> the type of the Right value
     */
    private static final class Right<L, R> extends Either<L, R> {
        private final R value;

        private Right(R value) {
            this.value = value;
        }

        @Override
        public boolean isLeft() {
            return false;
        }

        @Override
        public boolean isRight() {
            return true;
        }

        @Override
        public Optional<L> getLeft() {
            return Optional.empty();
        }

        @Override
        public Optional<R> getRight() {
            return Optional.of(value);
        }

        @Override
        public <T> Either<L, T> map(Function<? super R, ? extends T> mapper) {
            return Either.right(mapper.apply(value));
        }

        @Override
        public <T> Either<L, T> flatMap(Function<? super R, Either<L, T>> mapper) {
            return mapper.apply(value);
        }

        @Override
        public <T> T fold(Function<? super L, T> leftMapper, Function<? super R, T> rightMapper) {
            return rightMapper.apply(value);
        }
    }
}
