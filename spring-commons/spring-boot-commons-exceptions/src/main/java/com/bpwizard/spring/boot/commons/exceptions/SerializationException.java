package com.bpwizard.spring.boot.commons.exceptions;

/**
 * <p>Exception thrown when the Serialization process fails.</p>
 *
 */
public class SerializationException extends RuntimeException {
	private static final long serialVersionUID = -3412924037146749684L;

	/**
     * <p>Constructs a new {@code SerializationException} without specified
     * detail message.</p>
     */
    public SerializationException() {
    }

    /**
     * <p>Constructs a new {@code SerializationException} with specified
     * detail message.</p>
     *
     * @param msg  The error message.
     */
    public SerializationException(final String msg) {
        super(msg);
    }

    /**
     * <p>Constructs a new {@code SerializationException} with specified
     * nested {@code Throwable}.</p>
     *
     * @param cause  The {@code Exception} or {@code Error}
     *  that caused this exception to be thrown.
     */
    public SerializationException(final Throwable cause) {
        super(cause);
    }

    /**
     * <p>Constructs a new {@code SerializationException} with specified
     * detail message and nested {@code Throwable}.</p>
     *
     * @param msg    The error message.
     * @param cause  The {@code Exception} or {@code Error}
     *  that caused this exception to be thrown.
     */
    public SerializationException(final String msg, final Throwable cause) {
        super(msg, cause);
    }

}