package org.junder.structure;

import org.junder.process.JavaClassTraverser;
import org.junder.process.TraverseInput;
import org.junder.util.Messenger;

public class ProgramStructureFactory {
	
	private ProgramStructureFactory() {} // the factory cannot be constructed
	
	public static ProgramStructure create(TraverseInput input, Messenger messenger) throws Exception {
		JavaClassTraverser traverser = new JavaClassTraverser(input, messenger);
		ProgramStructureImpl result = new ProgramStructureImpl();
		traverser.forEach(result);
		return result;
	}

	public static ProgramStructure create(String inputFile, Messenger messenger) throws Exception {
		return create(new TraverseInput(inputFile,true), messenger);
	}
	
}
