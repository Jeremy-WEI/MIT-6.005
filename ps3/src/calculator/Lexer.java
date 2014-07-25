package calculator;

import calculator.Type;

/**
 * Calculator lexical analyzer.
 */
public class Lexer {

	/**
	 * Token in the stream.
	 */
	public static class Token {
		final Type type;
		final String text;

		Token(Type type, String text) {
			this.type = type;
			this.text = text;
		}

		Token(Type type) {
			this(type, null);
		}
	}

	@SuppressWarnings("serial")
	static class TokenMismatchException extends Exception {
	}

	// TODO write method spec
	public Lexer(String input) {
		// TODO implement for Problem 2
	}
}
