package org.laroche.multidispatch.compiler;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import java.io.File;
import java.io.FileFilter;
import java.util.Collections;
import java.util.Set;

/**
 * maven plugin entry point
 * <br />
 * Date: 26/09/12
 * Time: 14:03
 *
 * @author françois LAROCHE
 */
public abstract class MultiDispatchMojo extends AbstractMojo {
    protected abstract File getDirectory();

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        this.getLog().info("><><>< Begin of multidispatch ><><><");
        try {
            for(File classFile : listClasses(getDirectory())) {
                this.getLog().info("><><>< multidispatch on file " + classFile.getAbsolutePath());
                Compiler.compileFile(classFile);
            }
        } catch (Exception e) {
            throw new MojoFailureException(e, "Error while multi dispatching",
                    "An unexpected error occurred while executing multi dispatch");
        }
    }

    public Set<File> listClasses(File root) {
        if(root == null || !root.isDirectory()) {
            return Collections.emptySet();
        }
        Set<File> classes = Sets.newHashSet();
        classes.addAll(Lists.newArrayList(root.listFiles(CLASSES_FILTER)));
        for(File subDir : root.listFiles(DIRECTORIES_FILTER)) {
            classes.addAll(listClasses(subDir));
        }
        return classes;
    }

    private static final FileFilter CLASSES_FILTER = new FileFilter() {
        @Override
        public boolean accept(File pathname) {
            return !pathname.isDirectory() && pathname.getName().endsWith(".class");
        }
    };

    private static final FileFilter DIRECTORIES_FILTER = new FileFilter() {
        @Override
        public boolean accept(File pathname) {
            return pathname.isDirectory();
        }
    };
}
