package com.bpwizard.wcm.repo.validation;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.of;
import java.util.List;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EnumValidatorImpl implements ConstraintValidator<EnumValidator, String> {
	private List<String> valueList = null;

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		return valueList.contains(value.toUpperCase());
	}

	@Override
	public void initialize(EnumValidator constraintAnnotation) {
		valueList = of(constraintAnnotation.enumClazz().getEnumConstants()).map(e -> e.toString()).collect(toList());
	}
}