package thefellas.safepoint.impl.settings;

import thefellas.safepoint.Safepoint;
import thefellas.safepoint.impl.settings.impl.ParentSetting;
import thefellas.safepoint.impl.modules.Module;

import java.util.function.Predicate;

public class Setting<T> {
    public String name;
    public Module module;
    public T value;
    public Predicate<T> shown;
    public ParentSetting parentSetting;
    public boolean hasParentSetting = false;

    public Setting(String name, T value, Module module) {
        this.name = name;
        this.value = value;
        this.module = module;
        assert Safepoint.settingInitializer != null;
        Safepoint.settingInitializer.addSetting(this);
        module.settings.add(this);
    }

    public Setting(String name, T value, Module module, Predicate<T> shown) {
        this.name = name;
        this.value = value;
        this.module = module;
        this.shown = shown;
        assert Safepoint.settingInitializer != null;
        Safepoint.settingInitializer.addSetting(this);
        module.settings.add(this);
    }

    public void setValue(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public Module getModule() {
        return module;
    }

    public String getValueAsString() {
        return value.toString();
    }

    public boolean isVisible() {
        if (hasParentSetting && !parentSetting.isOpen)
            return false;

        if (shown == null)
            return true;

        return shown.test(getValue());
    }
}
