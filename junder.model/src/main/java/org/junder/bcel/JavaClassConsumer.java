package org.junder.bcel;

import org.apache.bcel.classfile.JavaClass;

@FunctionalInterface
public interface JavaClassConsumer {
	void accept(JavaClass parse) throws Exception;
}
