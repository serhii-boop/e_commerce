package dyplom.e_commerce.setting;

import dyplom.e_commerce.entities.setting.Setting;

import java.util.List;

public class SettingBag {
    private List<Setting> listSettings;

    public SettingBag(List<Setting> listSettings) {
        this.listSettings = listSettings;
    }

    public Setting get(String key) {
        //int index = listSettings.indexOf(new Setting(key));
        for (Setting setting: listSettings){
            if (setting.getKey().equals(key))
                return setting;
        }
        return null;
    }

    public String getValue(String key) {
        Setting setting = get(key);
        if (setting != null) {
            return setting.getValue();
        }

        return null;
    }

    public void update(String key, String value) {
        Setting setting = get(key);
        if (setting != null && value != null) {
            setting.setValue(value);
        }
    }

    public List<Setting> list() {
        return listSettings;
    }

}
