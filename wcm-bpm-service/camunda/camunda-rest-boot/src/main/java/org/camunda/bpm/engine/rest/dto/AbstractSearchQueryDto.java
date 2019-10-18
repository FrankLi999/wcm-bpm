/*
 * Copyright Camunda Services GmbH and/or licensed to Camunda Services GmbH
 * under one or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information regarding copyright
 * ownership. Camunda licenses this file to you under the Apache License,
 * Version 2.0; you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.camunda.bpm.engine.rest.dto;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.camunda.bpm.engine.rest.dto.converter.JacksonAwareStringToTypeConverter;
import org.camunda.bpm.engine.rest.dto.converter.StringToTypeConverter;
import org.camunda.bpm.engine.rest.exception.InvalidRequestException;
import org.camunda.bpm.engine.rest.exception.RestException;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Roman Smirnov
 *
 */
public abstract class AbstractSearchQueryDto {

	protected ObjectMapper objectMapper;

	// required for populating via jackson
	public AbstractSearchQueryDto() {

	}

	public AbstractSearchQueryDto(ObjectMapper objectMapper, Map<String, String[]> queryParameters) {
		this.objectMapper = objectMapper;
		for (Entry<String, String[]> param : queryParameters.entrySet()) {
			String key = param.getKey();
			String value = param.getValue()[0];
			this.setValueBasedOnAnnotation(key, value);
		}
	}

	// note: with Jackson version >= 1.9, it would be better to use @JacksonInject
	// and
	// configure the object mapper in the JacksonConfigurator class to be an
	// injectable value.
	// then, explicitly calling this method with every query is not necessary any
	// longer
	@JsonIgnore
	public void setObjectMapper(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	/**
	 * Finds the methods that are annotated with a {@link CamundaQueryParam} with a
	 * value that matches the key parameter. Before invoking these methods, the
	 * annotated {@link StringToTypeConverter} is used to convert the String value
	 * to the desired Java type.
	 * 
	 * @param key
	 * @param value
	 */
	protected void setValueBasedOnAnnotation(String key, String value) {
		List<Method> matchingMethods = findMatchingAnnotatedMethods(key);
		for (Method method : matchingMethods) {
			Class<? extends JacksonAwareStringToTypeConverter<?>> converterClass = findAnnotatedTypeConverter(method);
			if (converterClass == null) {
				continue;
			}

			JacksonAwareStringToTypeConverter<?> converter = null;
			try {
				converter = converterClass.newInstance();
				converter.setObjectMapper(objectMapper);
				Object convertedValue = converter.convertQueryParameterToType(value);
				method.invoke(this, convertedValue);
			} catch (InstantiationException e) {
				throw new RestException(HttpStatus.INTERNAL_SERVER_ERROR, e, "Server error.");
			} catch (IllegalAccessException e) {
				throw new RestException(HttpStatus.INTERNAL_SERVER_ERROR, e, "Server error.");
			} catch (InvocationTargetException e) {
				throw new InvalidRequestException(HttpStatus.BAD_REQUEST, e,
						"Cannot set query parameter '" + key + "' to value '" + value + "'");
			} catch (RestException e) {
				throw new InvalidRequestException(e.getStatus(), e,
						"Cannot set query parameter '" + key + "' to value '" + value + "': " + e.getMessage());
			}
		}
	}

	private List<Method> findMatchingAnnotatedMethods(String parameterName) {
		List<Method> result = new ArrayList<Method>();
		Method[] methods = this.getClass().getMethods();
		for (int i = 0; i < methods.length; i++) {
			Method method = methods[i];
			Annotation[] methodAnnotations = method.getAnnotations();

			for (int j = 0; j < methodAnnotations.length; j++) {
				Annotation annotation = methodAnnotations[j];
				if (annotation instanceof CamundaQueryParam) {
					CamundaQueryParam parameterAnnotation = (CamundaQueryParam) annotation;
					if (parameterAnnotation.value().equals(parameterName)) {
						result.add(method);
					}
				}
			}
		}
		return result;
	}

	private Class<? extends JacksonAwareStringToTypeConverter<?>> findAnnotatedTypeConverter(Method method) {
		Annotation[] methodAnnotations = method.getAnnotations();

		for (int j = 0; j < methodAnnotations.length; j++) {
			Annotation annotation = methodAnnotations[j];
			if (annotation instanceof CamundaQueryParam) {
				CamundaQueryParam parameterAnnotation = (CamundaQueryParam) annotation;
				return parameterAnnotation.converter();
			}
		}
		return null;
	}

}