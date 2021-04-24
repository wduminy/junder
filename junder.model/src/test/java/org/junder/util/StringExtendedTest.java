package org.junder.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class StringExtendedTest {

	@Test
	public void test() {
		assertEquals("One two three", StringExtended.toSentence("OneTwoThree"));
		assertEquals("One o", StringExtended.toSentence("OneO"));
	}

}
