package com.hackerearth.fullstack.backend.exception;

public class ErrorDetails {
	private String message;

	public ErrorDetails(String message) {
		this.message = message;
	}

	public ErrorDetails() {
	}

	public String getMessage() {
		return message;
	}
}
