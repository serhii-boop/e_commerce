package dyplom.e_commerce.controllers;

import dyplom.e_commerce.entities.setting.Setting;
import dyplom.e_commerce.entityService.SettingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class SettingController {

    private final SettingService settingService;

    public SettingController(SettingService settingService) {
        this.settingService = settingService;
    }

    @GetMapping("/admin-page/settings")
    public String listAll(Model model) {
        List<Setting> settingList = settingService.getAll();
        for (Setting setting: settingList) {
            model.addAttribute(setting.getKey(), setting.getValue());
        }
        model.addAttribute("pageTitle", "Settings");
        return "setting/settings";
    }

    private void updateSettingValuesFromForm(HttpServletRequest request, List<Setting> listSettings) {
        for (Setting setting : listSettings) {
            String value = request.getParameter(setting.getKey());
            if (value != null) {
                setting.setValue(value);
            }
        }
        settingService.saveAll(listSettings);
    }

    @PostMapping("/admin-page/settings/save_mail_server")
    public String saveMailServerSettings(HttpServletRequest request, RedirectAttributes ra) {
        List<Setting> mailServerSettings = settingService.getMailServerSettings();
        updateSettingValuesFromForm(request, mailServerSettings);
        ra.addFlashAttribute("message", "Mail server settings have been saved");
        return "redirect: /admin-page/settings";
    }

    @PostMapping("/admin-page/settings/save_mail_templates")
    public String saveMailTemplatesSettings(HttpServletRequest request, RedirectAttributes ra) {
        List<Setting> mailTemplSettings = settingService.getMailTemplatesSettings();
        updateSettingValuesFromForm(request, mailTemplSettings);
        ra.addFlashAttribute("message", "Mail templates server settings have been saved");
        return "redirect: /admin-page/settings";
    }
}
