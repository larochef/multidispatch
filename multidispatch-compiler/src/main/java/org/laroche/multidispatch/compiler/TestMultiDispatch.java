package org.laroche.multidispatch.compiler;

import java.io.File;

/**
 * Date: 26/09/12
 *
 * @author François LAROCHE
 * @goal test-compile
 * @execution phase=test-compile
 */
public class TestMultiDispatch extends MultiDispatchMojo {

    /**
     * @parameter default-value="${project.build.testOutputDirectory}"
     */
    private File testOutputDirectory;

    public void setTestOutputDirectory(File testOutputDirectory) {
        this.testOutputDirectory = testOutputDirectory;
    }

    @Override
    protected File getDirectory() {
        return testOutputDirectory;
    }
}
