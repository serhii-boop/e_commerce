package dyplom.e_commerce.entityService;

import dyplom.e_commerce.entities.setting.Setting;
import dyplom.e_commerce.entities.setting.SettingCategory;
import dyplom.e_commerce.repositories.SettingRepository;
import dyplom.e_commerce.setting.EmailSettingBag;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SettingService {
    private final SettingRepository settingRepository;

    public SettingService(SettingRepository settingRepository) {
        this.settingRepository = settingRepository;
    }

    public List<Setting> getAll() {
        return settingRepository.findAll();
    }

    public List<Setting> getMailTemplatesSettings() {
        return settingRepository.findAllByCategory(SettingCategory.MAIL_TEMPLATES);
    }

    public List<Setting> getMailServerSettings() {
        return settingRepository.findAllByCategory(SettingCategory.MAIL_SERVER);
    }

    public EmailSettingBag getEmailSettings() {
        List<Setting> settingList = settingRepository.findAllByCategory(SettingCategory.MAIL_SERVER);
        settingList.addAll(settingRepository.findAllByCategory(SettingCategory.MAIL_TEMPLATES));
        settingList.forEach(System.out::println);
        return new EmailSettingBag(settingList);
    }

    public void saveAll(List<Setting> settingList) {
        settingRepository.saveAll(settingList);
    }
}
