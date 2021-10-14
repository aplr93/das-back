package com.webdev.dasback.config.validation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/*
 * This class intercepts any exception during processing of API requests, and returns a much more friendly error message to the front end developer
 */
@RestControllerAdvice
public class ValidationErrorHandler {
	
	@Autowired
	private MessageSource messageSource;
	
	/*This makes any HTTP request processing error return a 400 return code (error) instead of 200, which was the default framework behavior,
	even after throwing exceptions*/
	@ResponseStatus(code = HttpStatus.BAD_REQUEST) 
	@ExceptionHandler(MethodArgumentNotValidException.class) 
	public List<ValidationError> handle(MethodArgumentNotValidException exception) {
		List<FieldError> fieldErrorLists = exception.getBindingResult().getFieldErrors();
		
		List<ValidationError> errorList = new ArrayList<ValidationError>();
		fieldErrorLists.forEach(fieldError ->{
			String errorMessage = messageSource.getMessage(fieldError, LocaleContextHolder.getLocale()); 
			ValidationError error = new ValidationError(fieldError.getField(), errorMessage);
			errorList.add(error);
		});
		
		return errorList;
	}
}
