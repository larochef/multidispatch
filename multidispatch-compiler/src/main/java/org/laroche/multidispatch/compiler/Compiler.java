package org.laroche.multidispatch.compiler;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import org.laroche.multidispatch.api.MultiDispatched;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;

/**
 * Compiler class : add the multi dispatch.
 * <br />
 * Date: 26/09/12
 * Time: 12:59
 *
 * @author françois LAROCHE
 */
public final class Compiler {

    private Compiler() {
        // do not instanciate utility class
    }

    private static final String ANNOTATION_DISPATCH_NAME = MultiDispatched.class.getName().replaceAll("\\.", "/");

    public static void compileFile(File file) throws Exception{

        InputStream str = new FileInputStream(file);
        ClassReader reader = new ClassReader(str);

        final List<String> classNames = Lists.newArrayList();

        final Multimap<String, String> methods = ArrayListMultimap.create();
        final List<String> multiDispachedMethods = Lists.newArrayList();

        reader.accept(new ClassVisitor(Opcodes.ASM4) {
            @Override
            public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
                classNames.add(name);
                super.visit(version, access, name, signature, superName, interfaces);
            }

            @Override
            public MethodVisitor visitMethod(int access, final String name, final String desc, String signature,
                                             String[] exceptions) {
                methods.put(name, desc);
                return new MethodVisitor(Opcodes.ASM4) {
                    @Override
                    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
                        if(desc.contains(ANNOTATION_DISPATCH_NAME)) {
                            multiDispachedMethods.add(name + desc);
                        }
                        return super.visitAnnotation(desc, visible);
                    }
                };
            }
        }, 0);

        Collection<String> keys = ImmutableList.copyOf(methods.keys());

        for(String key : keys) {
            if(!multiDispachedMethods.contains(key)) {
                methods.removeAll(key);
            }
        }

        final ClassWriter writer = new ClassWriter(0);
        final String className = classNames.get(0);

        ClassVisitor visitor = new ClassVisitor(Opcodes.ASM4, writer) {
            @Override
            public MethodVisitor visitMethod(int access, final String name, final String desc, String signature,
                                             String[] exceptions) {
                if(multiDispachedMethods.contains(name)) {
                    return new MethodVisitor(Opcodes.ASM4, super.visitMethod(access, name, desc, signature,
                            exceptions)) {
                        @Override
                        public void visitCode() {
                            if(multiDispachedMethods.contains(name + desc)) {
                                for(String description : methods.get(name)) {
                                    String castMeTo = parseName(description);
                                    if(castMeTo == null) {
                                        continue;
                                    }
                                    visitVarInsn(Opcodes.ALOAD, 1);
                                    visitTypeInsn(Opcodes.INSTANCEOF, castMeTo);
                                    Label l1 = new Label();
                                    visitJumpInsn(Opcodes.IFEQ, l1);
                                    visitVarInsn(Opcodes.ALOAD, 0);
                                    visitVarInsn(Opcodes.ALOAD, 1);
                                    visitTypeInsn(Opcodes.CHECKCAST, castMeTo);
                                    visitMethodInsn(Opcodes.INVOKEVIRTUAL, className, name, description);
                                    visitInsn(Opcodes.RETURN);
                                    visitLabel(l1);
                                }
                            }
                            super.visitCode();

                        }
                    };
                }
                return super.visitMethod(access, name, desc, signature, exceptions);

            }
        } ;

        reader.accept(visitor, 0);
        str.close();
        if(!multiDispachedMethods.isEmpty()) {
            byte[] result = writer.toByteArray();
            FileOutputStream fileWriter = new FileOutputStream(file);
            fileWriter.write(result);
            fileWriter.close();
        }
    }

    private static String parseName(String desc) {
        if(!desc.startsWith("(")) {
            return null;
        }
        if(!desc.contains(")")) {
            return null;
        }
        String parameters = desc.substring(1, desc.indexOf(")"));
        if(!parameters.startsWith("L")) {
            if(parameters.length() != 1) {
                return null;
            }
            return parameters;
        }
        if(!parameters.contains(";")) {
            return null;
        }
        String type = parameters.substring(1, parameters.indexOf(';'));
        if(type.length() + 2 != parameters.length()) {
            System.out.println("Multiple parameters found in parameters " + desc
                    + ", this method will not be handled");
            return null;
        }
        return type;
    }

}
