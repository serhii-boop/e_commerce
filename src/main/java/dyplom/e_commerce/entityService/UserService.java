package dyplom.e_commerce.entityService;

import dyplom.e_commerce.entities.User;
import dyplom.e_commerce.repositories.UserRepository;
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

    public void saveUser(User user){
        userRepository.save(user);
    }
}
