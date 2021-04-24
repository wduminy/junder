package org.junder.process;

final public class JavaClassName {
	private final String self;

	public JavaClassName(String wrapped) {
		self = wrapped;
	}

	public String getPackageName() {
		String full = self;
		int lastStop = full.lastIndexOf('.');
		if (lastStop != -1) 
			return full.substring(0,lastStop);
		else
			return "default";
	}

	public String getClassName() {
		String full = self;
		int lastStop = full.lastIndexOf('.');
		if (lastStop != -1)  {
			return full.substring(lastStop+1);
		} else
			return full;
	}

	public boolean isNested() {
		return self.indexOf('$') > -1;
	}

	public String nestedClassName() {
		String full = self;
		int lastStop = full.lastIndexOf('$');
		if (lastStop == -1) 
			throw new IllegalStateException("Name is not nested");
		return full.substring(0,lastStop);
	}
}
