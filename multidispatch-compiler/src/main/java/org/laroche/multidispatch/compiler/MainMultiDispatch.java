package org.laroche.multidispatch.compiler;

import java.io.File;

/**
 * Date: 26/09/12
 *
 * @author François LAROCHE
 * @goal compile
 * @execution phase=compile goal=compile
 */
public class MainMultiDispatch extends MultiDispatchMojo  {
    /**
     * @parameter default-value="${project.build.outputDirectory}"
     */
    private File outputDirectory;

    public void setOutputDirectory(File outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    @Override
    protected File getDirectory() {
        return outputDirectory;
    }
}
