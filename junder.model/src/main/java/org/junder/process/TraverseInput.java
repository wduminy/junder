package org.junder.process;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;

final public class TraverseInput {

	Optional<String> errorMessage = Optional.empty();
	private Set<File> inputPaths;

	public TraverseInput(String propertyFilepath, boolean throwWhenNotValid) throws Exception {
		this(propertyFilepath);
		if (throwWhenNotValid && !isValid()) {
			throw new RuntimeException("Validation error on: '" + propertyFilepath +"'.\n" + getErrorMessage());
		}

	}
	public TraverseInput(String propertyFilepath) {
		Objects.requireNonNull(propertyFilepath);
		try (FileInputStream in = new FileInputStream(propertyFilepath)) {
			Properties props = new Properties();
			props.load(in);
			String paths = props.getProperty("inputPaths");
			if (paths == null)
				errorMessage = Optional.of("A value for inputPaths is required.");
			else {
				Set<File> pathSet = new HashSet<>();
				for (String p : paths.split(",")) {
					File pf = new File(p.trim());
					if (!pf.exists())
						errorMessage = Optional.of("The path does not exist:" + pf.getAbsolutePath());
					pathSet.add(pf);
				}
				inputPaths = Collections.unmodifiableSet(pathSet);
				if (inputPaths.size() == 0)
					errorMessage = Optional.of("value for inputPaths has no entries");
			}
		} catch (FileNotFoundException e) {
			errorMessage = Optional.of("The file was not found:" + propertyFilepath);
		} catch (IOException e) {
			errorMessage = Optional.of("Reading the file caused an error:" + propertyFilepath +
					"\n"+ e.getMessage());
		}
	}
	
	public boolean isValid() {
		return !errorMessage.isPresent();
	}
	
	/**
	 * Returns empty string if {@link #isValid()} is false
	 * @return
	 */
	public String getErrorMessage() {
		return errorMessage.orElse("");
	}
	
	/**
	 * Returns an unmodifiable set of files that were verified to exist
	 * @return
	 */
	public Set<File> getPaths() {
		if (!isValid())
			throw new IllegalStateException("Cannot use this method on an invalid object");
		return inputPaths;
	}

}
