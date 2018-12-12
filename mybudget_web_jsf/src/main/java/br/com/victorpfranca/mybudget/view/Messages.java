package br.com.victorpfranca.mybudget.view;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class Messages {

	private static final String MESSAGE_FILE = "br.com.victorpfranca.mybudget.messages";

	public static String msg(Locale locale, String msg, Object... args) {
		return MessageFormat.format(msg(locale, msg), args);
	}

	public static String msg(String msg, Object... args) {
		return MessageFormat.format(msg(msg), args);
	}

	public static String msg(String msg) {
		ResourceBundle resourceBundle = ResourceBundle.getBundle(MESSAGE_FILE);
		return resolveMessageFromBundle(msg, resourceBundle);
	}

	public static String msg(Locale locale, String msg) {
		ResourceBundle resourceBundle = ResourceBundle.getBundle(MESSAGE_FILE, locale);
		return resolveMessageFromBundle(msg, resourceBundle);
	}

	private static String resolveMessageFromBundle(String msg, ResourceBundle resourceBundle) {
		if (resourceBundle.containsKey(msg)) {
			return resourceBundle.getString(msg);
		}
		return msg;
	}

}
