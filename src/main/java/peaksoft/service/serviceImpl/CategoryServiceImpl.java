package peaksoft.service.serviceImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import peaksoft.dto.request.CategoryRequest;
import peaksoft.dto.response.CategoryResponse;
import peaksoft.dto.response.MenuItemResponse;
import peaksoft.dto.SimpleResponse;
import peaksoft.entity.Category;
import peaksoft.entity.MenuItem;
import peaksoft.exceptions.AlreadyExistException;
import peaksoft.exceptions.NotFoundException;
import peaksoft.repository.CategoryRepository;
import peaksoft.repository.MenuItemRepository;
import peaksoft.service.CategoryService;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final MenuItemRepository menuItemRepository;

    @Override
    public SimpleResponse save(CategoryRequest category) {
        Category category1 = new Category();
        if (!categoryRepository.existsByName(category.name())) {
            category1.setName(category.name());
            categoryRepository.save(category1);
            return SimpleResponse.builder().status(HttpStatus.OK).message("Category with id: " + category1.getId() + " is saved!").build();
        } else {
            throw new AlreadyExistException("Category with name: " + category.name() + " is already exist!");
        }
    }

    @Override
    public List<CategoryResponse> getAll() {
        return categoryRepository.getAllCat();
    }

    @Override
    public CategoryResponse getById(Long id) {
        return categoryRepository.getCatById(id).orElseThrow(() -> new NotFoundException("Category with id: " + id + " is no exist!"));
    }

    @Override
    public SimpleResponse deleteById(Long id) {
        categoryRepository.findById(id).orElseThrow(() -> new NotFoundException("Category with id: " + id + " is no exist!"));
        List<MenuItem> menus = menuItemRepository.getByCategoryId(id);
        for (MenuItem menu : menus) {
            menu.setSubcategory(null);
            menuItemRepository.deleteById(menu.getId());
        }
        categoryRepository.deleteById(id);
        return SimpleResponse.builder().status(HttpStatus.OK).message("Category with id: " + id + " is deleted!").build();
    }

    @Override
    public SimpleResponse update(Long id, Category category) {
        Category category1 = categoryRepository.findById(id).orElseThrow(() -> new NotFoundException("Category with id: " + id + " is no exist!"));
        List<Category> all = categoryRepository.findAll();
        all.remove(category1);
        for (Category category2 : all) {
            if (category2.getName().equals(category1.getName())) {
                throw new AlreadyExistException("Category with name: " + category.getName() + " is already exist!");
            } else {
                category1.setName(category.getName());
                categoryRepository.save(category1);
                return SimpleResponse.builder().status(HttpStatus.OK).message("Category with id: " + id + " is updated!").build();
            }
        }
        return null;
    }
}
