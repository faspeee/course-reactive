package com.example.stream.spring.courses.reactive.example.functional;

import java.util.Optional;
import java.util.function.Function;

/**
 * Represents a value of one of two possible types (a disjoint union).
 * Instances of Either are either an instance of Left or Right.
 *
 * @param <L> the type of the Left value
 * @param <R> the type of the Right value
 */
public sealed interface Either<L, R> permits Either.Left, Either.Right {

    /**
     * Creates an instance of Left with the given value.
     *
     * @param value the Left value
     * @param <L>   the type of the Left value
     * @param <R>   the type of the Right value
     * @return an Either representing the Left value
     */
    static <L, R> Either<L, R> left(L value) {
        return new Left<>(value);
    }

    /**
     * Creates an instance of Right with the given value.
     *
     * @param value the Right value
     * @param <L>   the type of the Left value
     * @param <R>   the type of the Right value
     * @return an Either representing the Right value
     */
    static <L, R> Either<L, R> right(R value) {
        return new Right<>(value);
    }

    boolean isLeft();

    boolean isRight();

    Optional<L> getLeft();

    Optional<R> getRight();

    <T> Either<L, T> map(Function<? super R, ? extends T> mapper);

    <T> Either<L, T> flatMap(Function<? super R, ? extends Either<L, T>> mapper);

    <T> T fold(Function<? super L, T> leftMapper, Function<? super R, T> rightMapper);

    /**
     * Represents the Left value of an Either, typically used to hold an error or failure.
     *
     * @param <L> the type of the Left value
     * @param <R> the type of the Right value
     */
    record Left<L, R>(L value) implements Either<L, R> {

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
            return new Left<>(value);
        }

        @Override
        public <T> Either<L, T> flatMap(Function<? super R, ? extends Either<L, T>> mapper) {
            return new Left<>(value);
        }

        @Override
        public <T> T fold(Function<? super L, T> leftMapper, Function<? super R, T> rightMapper) {
            return leftMapper.apply(value);
        }
    }

    /**
     * Represents the Right value of an Either, typically used to hold a successful result.
     *
     * @param <L> the type of the Left value
     * @param <R> the type of the Right value
     */
    record Right<L, R>(R value) implements Either<L, R> {

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
            return new Right<>(mapper.apply(value));
        }

        @Override
        public <T> Either<L, T> flatMap(Function<? super R, ? extends Either<L, T>> mapper) {
            return mapper.apply(value);
        }

        @Override
        public <T> T fold(Function<? super L, T> leftMapper, Function<? super R, T> rightMapper) {
            return rightMapper.apply(value);
        }
    }

}
