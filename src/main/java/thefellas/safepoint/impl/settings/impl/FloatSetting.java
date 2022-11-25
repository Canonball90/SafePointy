package thefellas.safepoint.impl.settings.impl;

import thefellas.safepoint.impl.modules.Module;
import thefellas.safepoint.impl.settings.Setting;

import java.util.function.Predicate;

public class FloatSetting extends Setting<Float> {

    float minimum;
    float maximum;

    public FloatSetting(String name, float value, float minimum, float maximum, Module module) {
        super(name, value, module);
        this.minimum = minimum;
        this.maximum = maximum;
    }

    public FloatSetting(String name, float value, float minimum, float maximum, Module module, Predicate<Float> shown) {
        super(name, value, module, shown);
        this.minimum = minimum;
        this.maximum = maximum;
    }

    public Float getValue() {
        return value;
    }

    public float getMaximum() {
        return maximum;
    }

    public float getMinimum() {
        return minimum;
    }

    public FloatSetting setParent(ParentSetting parentSetting){
        this.parentSetting = parentSetting;
        hasParentSetting = true;

        return this;
    }
}
