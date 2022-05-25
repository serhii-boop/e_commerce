package dyplom.e_commerce.repositories;

import dyplom.e_commerce.entities.Category;
import dyplom.e_commerce.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface CategoryRepository extends PagingAndSortingRepository<Category, Integer> {
    Long countById(Integer id);
    @Query(value = "update Category c SET c.enabled = ?2 where c.id = ?1")
    @Modifying
    void updateEnabledStatus(Integer id, boolean enabled);

    Category getCategoryByName(String name);

    @Query(value = "select c from Category c where c.name LIKE %?1% or c.alias LIKE %?1%")
    Page<Category> findAll(String keyword, Pageable pageable);

    @Query("select c from Category  c where  c.enabled = true order by c.name ASC ")
    List<Category> findAllEnabled();
}
