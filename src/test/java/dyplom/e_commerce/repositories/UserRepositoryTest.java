package dyplom.e_commerce.repositories;

import dyplom.e_commerce.entities.Role;
import dyplom.e_commerce.entities.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;
    @Autowired
    private TestEntityManager entityManager;
    @Test
    public void createTest() {
        Role role = entityManager.find(Role.class, 1);
        User user = new User();
        user.setEmail("serhiydutchyn@gmail.com");
        user.setPassword("12345");
        user.setFirstName("Serhii");
        user.setLastName("Dutchyn");
        user.addRole(role);
        User savedUser = userRepository.save(user);
        assertThat(savedUser.getId()).isGreaterThan(0);
    }
}