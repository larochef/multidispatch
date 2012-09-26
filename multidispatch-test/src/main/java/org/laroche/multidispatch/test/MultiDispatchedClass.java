package org.laroche.multidispatch.test;

/**
 * Date: 26/09/12
 *
 * @author François LAROCHE
 */
public class MultiDispatchedClass {

    private Object objectValue;
    private Integer integerValue;

    @org.laroche.multidispatch.api.MultiDispatched
    public void doSomething(Object objectValue) {
        this.objectValue = objectValue;
    }

    public void doSomething(Integer integerValue) {
        this.integerValue = integerValue;
    }

    public Object getObjectValue() {
        return objectValue;
    }

    public Integer getIntegerValue() {
        return integerValue;
    }
}
