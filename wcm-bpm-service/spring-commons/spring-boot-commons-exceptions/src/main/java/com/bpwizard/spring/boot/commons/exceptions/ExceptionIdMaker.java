package com.bpwizard.spring.boot.commons.exceptions;

@FunctionalInterface
public interface ExceptionIdMaker {

	String make(Throwable t);
}
