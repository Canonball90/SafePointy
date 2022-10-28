package thefellas.safepoint.settings.impl;

import thefellas.safepoint.modules.Module;
import thefellas.safepoint.settings.Setting;

import java.util.List;
import java.util.function.Predicate;

public final class EnumSetting extends Setting<String> {
    final List<String> modes;
    public boolean droppedDown = false;

    public EnumSetting(String name, String value, List<String> modeList, Module module) {
        super(name, value, module);
        this.modes = modeList;
    }

    public EnumSetting(String name, String value, List<String> modeList, Module module, Predicate<String> shown) {
        super(name, value, module, shown);
        this.modes = modeList;
    }
    public List<String> getModes() {
        return this.modes;
    }

    public void setValue(String value) {
        this.value = (modes.contains(value) ? value : this.value);
    }

    public EnumSetting setParent(ParentSetting parentSetting){
        this.parentSetting = parentSetting;
        hasParentSetting = true;

        return this;
    }
}
