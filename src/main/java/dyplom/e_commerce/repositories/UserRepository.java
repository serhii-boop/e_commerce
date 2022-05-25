package dyplom.e_commerce.repositories;

import dyplom.e_commerce.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;



public interface UserRepository extends PagingAndSortingRepository<User, Integer> {
    User getByEmail(String email);

    Long countById(Integer id);

    @Query(value = "update User u SET u.enabled = ?2 where u.id = ?1")
    @Modifying
    void updateEnabledStatus(Integer id, boolean enabled);

    @Query(value = "select u from User u where u.firstName LIKE %?1% or u.lastName LIKE %?1%" +
            "or u.email LIKE %?1%")
    Page<User> findAll(String keyword, Pageable pageable);
}
