package org.laroche.multidispatch.compiler;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import java.io.File;

/**
 * maven plugin entry point
 * <br />
 * Date: 26/09/12
 * Time: 14:03
 *
 * @author françois LAROCHE
 */
public class MultiDispatchMojo extends AbstractMojo {

    private File outputDirectory;
    private File testOutputDirectory;

    public void setOutputDirectory(File outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    public void setTestOutputDirectory(File testOutputDirectory) {
        this.testOutputDirectory = testOutputDirectory;
    }

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {

    }
}
