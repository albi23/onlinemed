package com.onlinemed.functionalUtils;

import java.util.function.Consumer;

/**
 * A class that provides implementations of methods defined as lambda
 */
public abstract class FunctionalUtils {

    public static <T> Consumer<T> throwingConsumerWrapper(ThrowingConsumer<T, Exception> throwingConsumer) {
        return i -> {
            try {
                throwingConsumer.accept(i);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        };
    }

    public static <R, E extends Exception> R throwingSupplierWrapper(ThrowingSupplier<R, E> throwingSupplier,
                                                                     Class<E> exClass) {
        try {
            return throwingSupplier.apply();
        } catch (Exception ex) {
            try {
                final E cast = exClass.cast(ex);
                throw new RuntimeException(cast);
            } catch (ClassCastException ccEx) {
                throw new RuntimeException(ex);
            }
        }
    }

    public static <R, E extends Exception> R optionalSupplierWrapper(ThrowingSupplier<R, E> supplier, R orElseValue) {
        try {
            return supplier.apply();
        } catch (Exception ex) {
            ex.printStackTrace();
            return orElseValue;
        }
    }
}
