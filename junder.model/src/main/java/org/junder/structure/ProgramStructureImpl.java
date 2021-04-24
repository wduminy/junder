package org.junder.structure;

import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;

import org.apache.bcel.Const;
import org.apache.bcel.classfile.ConstantCP;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.classfile.Field;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.INVOKESTATIC;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.LocalVariableGen;
import org.apache.bcel.generic.MethodGen;
import org.apache.bcel.generic.NEW;
import org.apache.bcel.generic.ObjectType;
import org.apache.bcel.generic.Type;
import org.junder.process.JavaClassConsumer;
import org.junder.process.JavaClassName;

public class ProgramStructureImpl implements JavaClassConsumer, ProgramStructure {
	HashMap<String, JavaPackageImpl> packages = new HashMap<>();
	HashMap<String, JavaClassImpl> classes = new HashMap<>();

	@Override
	public void accept(JavaClass clazz) throws Exception {
		final JavaClassImpl c = obtainClass(clazz.getClassName());
		c.getPackage().setParsed();
		final String superClazz = clazz.getSuperclassName();
		if (!superClazz.equals("java.lang.Object"))
			c.addUsage(obtainClass(superClazz));
		for (String s : clazz.getInterfaceNames())
			c.addUsage(obtainClass(s));
		for (Field e : clazz.getFields())
			addUsageIfValid(c, e.getType());
		final ConstantPool constantPool = clazz.getConstantPool();
		final ConstantPoolGen cpgen = new ConstantPoolGen(constantPool);
		for (Method m : clazz.getMethods()) {
			addUsageIfValid(c, m.getReturnType());
			for (Type a : m.getArgumentTypes())
				addUsageIfValid(c, a);
			// get types used inside the method
			final MethodGen mg = new MethodGen(m, m.getName(), cpgen);
			for (LocalVariableGen lv : mg.getLocalVariables())
				addUsageIfValid(c, lv.getType());
			InstructionList instructions = mg.getInstructionList();
			if (instructions != null) {
				for (InstructionHandle ih = instructions.getStart(); ih != null; ih = ih.getNext()) {
					Instruction ins = ih.getInstruction();
					Integer classIndex = null;
					if (ins instanceof NEW) {
						NEW newins = (NEW) ins;
						classIndex = newins.getIndex();
					}
					if (ins instanceof INVOKESTATIC) {
						INVOKESTATIC invins = (INVOKESTATIC) ins;
						ConstantCP ref = (ConstantCP) constantPool
								.getConstant(invins.getIndex());
						classIndex = ref.getClassIndex();
					}
					if (classIndex != null) {
						String className = constantPool.getConstantString(
								classIndex,
								Const.CONSTANT_Class);
						c.addUsage(obtainClass(className.replace('/', '.')));
					}
				}

			}

		}

	}
	
	private void addUsageIfValid(JavaClassImpl c, Type type) {
		Objects.requireNonNull(c);
		Objects.requireNonNull(type);
		if (type instanceof ObjectType) {
			c.addUsage(obtainClass(
					ObjectType.class.cast(type).getClassName()
					));
		}
	}


	private JavaClassImpl obtainClass(String className) {
		JavaClassImpl r = classes.get(className);
		if (r == null) {
			JavaClassName clazzName = new JavaClassName(className);
			final JavaPackageImpl p = obtainPackage(clazzName.getPackageName());
			r = new JavaClassImpl(p, clazzName);
			classes.put(className, r);
			p.addClass(r);
		}
		return r;
	}

	private JavaPackageImpl obtainPackage(String packageName) {
		JavaPackageImpl r = packages.get(packageName);
		if (r == null) {
			r = new JavaPackageImpl(packageName);
			packages.put(packageName, r);
		}
		return r;
	}

	@Override
	public Collection<? extends JavaPackage> getPackages() {
		return packages.values();
	}

}
