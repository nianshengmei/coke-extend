

package org.needcoke.coke.http;

/**
 * Exceptions thrown by an web server.
 */
@SuppressWarnings("serial")
public class WebServerException extends RuntimeException {

	public WebServerException(String message, Throwable cause) {
		super(message, cause);
	}

	public WebServerException(String message) {
		super(message);
	}
}
