package org.junder.process;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class InputTests {
	
	@Test
	public void inputNotValid1() {
		TraverseInput e = new TraverseInput("testInput/invalid1.properties");
		assertEquals(false, e.isValid());
	}
	
	@Test
	public void inputNotValid2() {
		TraverseInput e = new TraverseInput("testInput/invalid2.properties");
		assertEquals(false, e.isValid());
	}
	
	@Test
	public void inputNotValid3() {
		TraverseInput e = new TraverseInput("testInput/badFilename.properties");
		assertEquals(false, e.isValid());
	}
	
	@Test
	public void inputNotValid4() {
		TraverseInput e = new TraverseInput("testInput/badPath.properties");
		assertEquals(false, e.isValid());
	}
	
}
