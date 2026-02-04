package com.kweezy.singeditor.app;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public final class Result<T> {
    private final boolean success;
    private final Optional<T> value;
    private final Optional<String> errorMessage;
    private final Optional<Throwable> cause;

    private Result(boolean success, Optional<T> value, Optional<String> errorMessage, Optional<Throwable> cause) {
        this.success = success;
        this.value = value;
        this.errorMessage = errorMessage;
        this.cause = cause;
    }

    public static <T> Result<T> ok(T value) {
        return new Result<>(true, Optional.of(Objects.requireNonNull(value, "value")), Optional.empty(), Optional.empty());
    }

    public static Result<Void> ok() {
        return new Result<>(true, Optional.empty(), Optional.empty(), Optional.empty());
    }

    public static <T> Result<T> fail(String errorMessage) {
        return fail(errorMessage, null);
    }

    public static <T> Result<T> fail(String errorMessage, Throwable cause) {
        Objects.requireNonNull(errorMessage, "errorMessage");
        return new Result<>(false, Optional.empty(), Optional.of(errorMessage), Optional.ofNullable(cause));
    }

    public boolean isOk() {
        return success;
    }

    public Optional<T> getValue() {
        return value;
    }

    public Optional<String> getErrorMessage() {
        return errorMessage;
    }

    public Optional<Throwable> getCause() {
        return cause;
    }

    public Result<T> ifOk(Consumer<T> consumer) {
        if (success) {
            value.ifPresent(consumer);
        }
        return this;
    }

    public Result<T> ifError(Consumer<String> consumer) {
        if (!success) {
            errorMessage.ifPresent(consumer);
        }
        return this;
    }

    public <R> Result<R> map(Function<T, R> mapper) {
        Objects.requireNonNull(mapper, "mapper");
        if (!success) {
            return Result.fail(errorMessage.orElse("Unknown error"), cause.orElse(null));
        }
        return Result.ok(mapper.apply(value.orElseThrow()));
    }
}
