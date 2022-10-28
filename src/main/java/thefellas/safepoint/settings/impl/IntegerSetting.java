package thefellas.safepoint.settings.impl;

import thefellas.safepoint.modules.Module;
import thefellas.safepoint.settings.Setting;

import java.util.function.Predicate;

public class IntegerSetting extends Setting<Integer> {

    int minimum;
    int maximum;

    public IntegerSetting(String name, int value, int minimum, int maximum, Module module) {
        super(name, value, module);
        this.minimum = minimum;
        this.maximum = maximum;
    }

    public IntegerSetting(String name, int value, int minimum, int maximum, Module module, Predicate<Integer> shown) {
        super(name, value, module, shown);
        this.minimum = minimum;
        this.maximum = maximum;
    }

    public Integer getValue() {
        return value;
    }

    public int getMaximum() {
        return maximum;
    }

    public int getMinimum() {
        return minimum;
    }

    public IntegerSetting setParent(ParentSetting parentSetting){
        this.parentSetting = parentSetting;
        hasParentSetting = true;

        return this;
    }
}
