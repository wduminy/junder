package org.junder.util;

import java.io.File;
import java.io.IOException;

public class FileExtended {
	public static boolean isValid(File self) {
		try {
			self.getCanonicalFile();
			return true;
		} catch (IOException e) {
			return false;
		}
	}
}
