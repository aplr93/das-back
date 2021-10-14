package com.webdev.dasback.config.validation;

/*
 * This class represents each HTTP request field that didn't fit back end expectations
 */
public class ValidationError {

	private String fieldName;
	private String fieldError;
	public ValidationError(String fieldName, String fieldError) {
		super();
		this.fieldName = fieldName;
		this.fieldError = fieldError;
	}
	public String getFieldName() {
		return fieldName;
	}
	public String getFieldError() {
		return fieldError;
	}
}
