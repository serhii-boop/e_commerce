package dyplom.e_commerce.entityService;

import dyplom.e_commerce.entities.Role;
import dyplom.e_commerce.repositories.RoleRepository;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role findByName(String name) {
        return roleRepository.findByName(name)
                .orElseThrow(RuntimeException::new);
    }
}
