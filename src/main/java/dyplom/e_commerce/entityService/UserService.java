package dyplom.e_commerce.entityService;

import dyplom.e_commerce.entities.User;
import dyplom.e_commerce.repositories.UserRepository;
import dyplom.e_commerce.security.SecurityUser;
import dyplom.e_commerce.security.UserDetailsServiceImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserById(int id){
        return userRepository.findById(id)
                .orElseThrow(RuntimeException::new);
    }

    public User getUserByEmail(String email){
        return userRepository.findByEmail(email)
                .orElseThrow(RuntimeException::new);
    }



//    public void saveUserRole(Integer roleId, Integer userId){
//        UsersRoles role = new UsersRoles();
//        role.setRoleId(roleId);
//        role.setUserId(userId);
//        userRolesRepository.save(role);
//    }

    public void saveUser(User user){
        userRepository.save(user);
    }
}
