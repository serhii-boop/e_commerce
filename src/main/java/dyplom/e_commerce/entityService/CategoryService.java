package dyplom.e_commerce.entityService;

import dyplom.e_commerce.entities.Category;
import dyplom.e_commerce.entities.Role;
import dyplom.e_commerce.entities.User;
import dyplom.e_commerce.repositories.CategoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CategoryService {

    public final static int CATEGORY_PER_PAGE = 5;

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> categoryList() {
        return (List<Category>) categoryRepository.findAll();
    }

    public Category getCategoryByName(String category) {
        return categoryRepository.getCategoryByName(category);
    }

    public Category saveCategory(Category category) {
//        Category parent = category.getParent();
//        if (parent != null) {
//            String  allParentIds = parent.getAllParentIDs() == null ? "-" : parent.getAllParentIDs();
//            allParentIds += String.valueOf(parent.getId()) + "-";
//            category.setAllParentIDs(allParentIds);
//        }
        return categoryRepository.save(category);
    }


    public boolean isCategoryUnique(Integer id, String category) {
        Category categoryByName = categoryRepository.getCategoryByName(category);
        if (categoryByName == null) return true;
        boolean isCreatingNew = (id == null);
        if (isCreatingNew) {
            if (categoryByName != null) {return false;}
        } else {
            if (categoryByName.getId() != id) {
                return false;
            }
        }
        return true;
    }

    public List<Category> listAll() {
        return (List<Category>) categoryRepository.findAll();
    }

    public Category getCategoryById(int id){
        return categoryRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    public void delete(Integer id){
        Long countById = categoryRepository.countById(id);
        if (countById == null || countById == 0) {
            throw new RuntimeException();
        }
        categoryRepository.deleteById(id);
    }

    public void updateCategoryEnabledStatus(Integer id, boolean enabled) {
        categoryRepository.updateEnabledStatus(id, enabled);
    }

    public Page<Category> listByPage(int pageNum, String sortField, String sortDir, String keyword){
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(pageNum - 1, CATEGORY_PER_PAGE, sort);
        if (keyword != null) {
            return categoryRepository.findAll(keyword, pageable);
        }
        return categoryRepository.findAll(pageable);
    }
}
