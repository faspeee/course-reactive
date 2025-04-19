package com.example.stream.spring.courses.reactive.example.utility;

import com.example.stream.spring.courses.reactive.example.functional.Either;
import com.example.stream.spring.courses.reactive.example.model.error.Error;
import com.example.stream.spring.courses.reactive.example.model.error.GenericError;
import reactor.core.publisher.Mono;

/**
 * Utility class for handling {@link Mono} instances in conjunction with {@link Either} types,
 * particularly focusing on error propagation within reactive streams.
 * <p>
 * This class provides methods to seamlessly integrate error handling by converting {@link Either}
 * instances into {@link Mono} sequences that emit appropriate error signals.
 * </p>
 *
 * <p><strong>Usage Example:</strong></p>
 * <pre>{@code
 * Either<Error, String> result = performOperation();
 * Mono<Either<Error, String>> monoResult = UtilMono.createMonoWithError(result);
 * }</pre>
 *
 * @author
 * @version 1.0
 * @since 2025-04-18
 */
public final class UtilMono {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private UtilMono() {
        // Utility class; prevent instantiation.
    }

    /**
     * Converts an {@link Either} instance into a {@link Mono} that emits the {@link Either} as-is
     * if it contains a {@code Right} value, or emits a {@code Left} value wrapped in a {@link Mono}
     * if present. If the {@code Left} value is absent, a default {@link GenericError} is used.
     *
     * <p>This method is particularly useful in reactive pipelines where operations may result in
     * an {@link Either} representing success or failure, and there's a need to propagate errors
     * appropriately within a {@link Mono} sequence.</p>
     *
     * @param <I>                the type of the input value contained in the {@code Either}
     * @param <O>                the type of the output value to be emitted by the {@code Mono}
     * @param errorBooleanEither the {@code Either} instance containing either an error or a value
     * @return a {@code Mono} emitting the {@code Either} with the appropriate error handling
     */
    public static <I, O> Mono<Either<Error, O>> createMonoWithError(Either<Error, I> errorBooleanEither) {
        return Mono.just(Either.left(errorBooleanEither.getLeft().orElse(new GenericError())));
    }
}

