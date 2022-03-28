package com.onlinemed.functionalUtils;

@FunctionalInterface
public interface ThrowingSupplier<R, E extends Exception> {
    R apply() throws E;
}
