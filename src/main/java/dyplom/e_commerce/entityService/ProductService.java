package dyplom.e_commerce.entityService;

import dyplom.e_commerce.entities.Product;
import dyplom.e_commerce.repositories.ResistorRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class ProductService {
    private final ResistorRepository resistorRepository;

    public ProductService(ResistorRepository resistorRepository) {
        this.resistorRepository = resistorRepository;
    }

    public Product getProductById(int id) {
        return resistorRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    public Page<Product> getResistorsList(int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber - 1,3);
        return resistorRepository.getAllByCategory("resistor", pageable);
    }

    public Product saveResistor(Product product) {
        resistorRepository.save(product);
        return product;
    }

//    public Page<Product> findPaginatedResistors(Pageable pageable) {
//        List<Product> resistors = getResistorsList();
//        int pageSize = pageable.getPageSize();
//        int currentPage = pageable.getPageNumber();
//        int startItem = currentPage * pageSize;
//        List<Product> list;
//
//        if (resistors.size() < startItem) {
//            list = Collections.emptyList();
//        } else {
//            int toIndex = Math.min(startItem + pageSize, resistors.size());
//            list = resistors.subList(startItem, toIndex);
//        }
//
//        Page<Product> resistorPage
//                = new PageImpl<Product>(list, PageRequest.of(currentPage, pageSize), resistors.size());
//
//        return resistorPage;
//    }
}
