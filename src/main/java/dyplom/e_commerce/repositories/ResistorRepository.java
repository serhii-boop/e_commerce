package dyplom.e_commerce.repositories;

import dyplom.e_commerce.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResistorRepository extends PagingAndSortingRepository<Product, Integer> {
    Page<Product> getAllByCategory(String category, Pageable pageable);
}
