package dyplom.e_commerce.entityService;

import dyplom.e_commerce.entities.Category;
import dyplom.e_commerce.entities.Product;
import dyplom.e_commerce.repositories.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class ProductService {

    public final static int PRODUCT_PER_PAGE = 10;
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> listAll() {
        return (List<Product>) productRepository.findAll();
    }

    public Page<Product> listByPage(int pageNum, String sortField, String sortDir, String keyword, Integer categoryId){
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(pageNum - 1, PRODUCT_PER_PAGE, sort);
        if (keyword != null && !keyword.isEmpty()) {
            return productRepository.findAll(keyword, pageable);
        }
        if (categoryId != null && categoryId > 0) {
            return productRepository.findAllInCategory(categoryId, pageable);
        }
        return productRepository.findAll(pageable);
    }

    public Product save(Product product) {
        if (product.getId() == null) {
            product.setCreatedTime(new Date());
        }

        product.setUpdatedTime(new Date());
        return productRepository.save(product);
    }

    public Product findByName(String name){
        return productRepository.findByName(name);
    }

    public void updateProductEnabledStatus(Integer id, boolean enabled) {
        productRepository.updateEnabledStatus(id, enabled);
    }

    public void delete(Integer id){
        Long countById = productRepository.countById(id);
        if (countById == null || countById == 0) {
            throw new RuntimeException();
        }
        productRepository.deleteById(id);
    }

    public Product getById(Integer id) {
        return productRepository.findById(id).get();
    }

}
