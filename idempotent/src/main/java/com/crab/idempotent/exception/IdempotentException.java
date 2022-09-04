package com.crab.idempotent.exception;

/**
 * Idempotent exception
 * By catch this exception, means occur idempotent limit.
 *
 * @author hackdc
 * @Date 2022/8/24 8:41 PM
 */
public class IdempotentException extends RuntimeException {

    public IdempotentException(String message) {
        super(message);
    }

}
