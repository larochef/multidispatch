package org.laroche.multidispatch.test;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Date: 26/09/12
 *
 * @author François LAROCHE
 */
public class TestMultiDispatch {

    @Test
    public void doTest() {

        Object obj = Integer.valueOf(10);
        MultiDispatchedClass dispatched = new MultiDispatchedClass();
        dispatched.doSomething(obj);
        Assert.assertNull(dispatched.getObjectValue());
        Assert.assertNotNull(dispatched.getIntegerValue());

    }

}
