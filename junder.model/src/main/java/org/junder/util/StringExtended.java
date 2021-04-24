package org.junder.util;

public class StringExtended {

	public static String toSentence(String string) {
		StringBuilder sb = new StringBuilder();
		int start = 0;
		for (int i = 1; i < string.length(); i++) {
			char c = string.charAt(i);
			if (Character.isUpperCase(c)) {
				addLowerCaseWord(sb, start, string.substring(start, i));
				start = i;
			}
		}
		addLowerCaseWord(sb, start, string.substring(start));
		return sb.toString();
	}

	private static void addLowerCaseWord(StringBuilder sb, int start, String word) {
		if (start > 0) {
			sb.append(" ");
			word = Character.toLowerCase(word.charAt(0)) + word.substring(1);
		}
		sb.append(word);
	}

}
