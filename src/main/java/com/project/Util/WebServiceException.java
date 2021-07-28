package com.project.Util;

public class WebServiceException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private RetornoWS error;

	public WebServiceException() {
		super();
	}

	public WebServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public WebServiceException(Throwable cause) {
		super(cause);
	}

	public WebServiceException(String message) {
		super(message);
	}

	public WebServiceException(String message, Throwable cause, RetornoWS error) {
		super(message, cause);
		this.error = error;
	}

	public RetornoWS getError() {
		return error;
	}
}
