package dyplom.e_commerce.repositories;

import dyplom.e_commerce.entities.setting.Setting;
import dyplom.e_commerce.entities.setting.SettingCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SettingRepository extends JpaRepository<Setting, String> {


    List<Setting> findAllByCategory(SettingCategory category);
}
