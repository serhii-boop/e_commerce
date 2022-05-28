package dyplom.e_commerce.repositories;

import dyplom.e_commerce.entities.Category;
import dyplom.e_commerce.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ProductRepository extends PagingAndSortingRepository<Product, Integer> {

    Product findByName(String name);

    Long countById(Integer id);
    @Query(value = "update Product p SET p.enabled = ?2 where p.id = ?1")
    @Modifying
    void updateEnabledStatus(Integer id, boolean enabled);

    @Query(value = "select p from Product p where p.name LIKE %?1% or p.shortDescription like %?1%")
    Page<Product> findAll(String keyword, Pageable pageable);

    @Query("select p from Product  p where p.category.id = ?1")
    Page<Product> findAllByCategory(Integer categoryId, Pageable pageable);

    @Query(value = "SELECT p FROM Product p WHERE p.enabled = true AND p.name LIKE %?1% or p.shortDescription LIKE %?1% or p.fullDescription LIKE %?1%")
    Page<Product> search(String keyword, Pageable pageable);
}
