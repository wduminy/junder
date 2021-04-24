package org.junder.process;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.apache.bcel.classfile.ClassParser;
import org.junder.util.Messenger;

public final class JavaClassTraverser {
	private final Messenger messenger;
	private final TraverseInput input;
	
	public JavaClassTraverser(TraverseInput i,Messenger m) throws IOException {
		Objects.requireNonNull(i);
		Objects.requireNonNull(m);
		if (!i.isValid())
			throw new IllegalArgumentException("input must be valid");
		messenger = m;
		input = i;
	}
	
	public void forEach(JavaClassConsumer v) throws Exception  {
		for (File p : input.getPaths())
			traverse(p,v);
	}

	private void traverse(File f,JavaClassConsumer v) throws Exception  {
		if (f.isDirectory())
			for (File c : f.listFiles())
				traverse(c,v);
		else if (f.getName().toLowerCase().endsWith(".jar")) 
			traverseJar(f,v);
		else if (f.getName().toLowerCase().endsWith(".class"))
			traverseClass(f,v);
		else
			messenger.info("Skipping: " + f.getAbsolutePath());
	}

	private void traverseClass(File f, JavaClassConsumer v) throws Exception {
		messenger.info("Processing Class: " + f.getAbsolutePath());
		InputStream fileStream = new FileInputStream(f);
		traverse(fileStream, f.getAbsolutePath(),v);
	}

	private void traverseJar(File f, JavaClassConsumer v) throws Exception  {
		messenger.info("Processing Archive: " + f.getAbsolutePath());
		try (ZipFile jar = new ZipFile(f)) {
			Enumeration<? extends ZipEntry> it = jar.entries();
			while (it.hasMoreElements()) {
				ZipEntry e = it.nextElement();
				if (!e.isDirectory() && e.getName().endsWith(".class"))
					traverse(jar.getInputStream(e),e.getName(),v);
			}
		}
	}
	
	private void traverse(InputStream fileStream, String filename, JavaClassConsumer v) throws Exception {
		ClassParser p = new ClassParser(fileStream,filename);
		v.accept(p.parse());
	}
	
}
