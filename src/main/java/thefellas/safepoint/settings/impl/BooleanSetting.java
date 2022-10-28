package thefellas.safepoint.settings.impl;


import thefellas.safepoint.modules.Module;
import thefellas.safepoint.settings.Setting;

import java.util.function.Predicate;

public class BooleanSetting extends Setting<Boolean> {

    public BooleanSetting(String name, Boolean value, Module module) {
        super(name, value, module);
    }

    public BooleanSetting(String name, boolean value, Module module, Predicate<Boolean> shown) {
        super(name, value, module, shown);
    }

    public Boolean getValue() {
        return value;
    }

    public void toggleValue() {
        value = !value;
    }

    public BooleanSetting setParent(ParentSetting parentSetting) {
        this.parentSetting = parentSetting;
        hasParentSetting = true;

        return this;
    }
}
