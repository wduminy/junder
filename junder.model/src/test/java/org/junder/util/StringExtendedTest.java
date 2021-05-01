package org.junder.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class StringExtendedTest {

	@Test
	public void test() {
		assertEquals("One two three", StringExtended.toSentence("OneTwoThree"));
		assertEquals("One o", StringExtended.toSentence("OneO"));
	}

}
