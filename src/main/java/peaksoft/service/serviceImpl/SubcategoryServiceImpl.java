package peaksoft.service.serviceImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import peaksoft.dto.SimpleResponse;
import peaksoft.dto.request.SubcategoryRequest;
import peaksoft.dto.response.SubcategoryResponse;
import peaksoft.entity.Category;
import peaksoft.entity.Subcategory;
import peaksoft.exceptions.NotFoundException;
import peaksoft.repository.CategoryRepository;
import peaksoft.repository.SubcategoryRepository;
import peaksoft.service.SubcategoryService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class SubcategoryServiceImpl implements SubcategoryService {
    private final SubcategoryRepository subcategoryRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public SimpleResponse save(SubcategoryRequest request) {
        if (!subcategoryRepository.existsByName(request.name())) {
            Subcategory sb = new Subcategory();
            Category category = categoryRepository.findById(request.categoryId()).orElseThrow(() -> new NotFoundException("Category with id: " + request.categoryId() + " is no exist!"));
            sb.setName(request.name());
            sb.setCategory(category);
            category.addSubcategories(sb);
            subcategoryRepository.save(sb);
            return SimpleResponse.builder().status(HttpStatus.OK).message("Subcategory with id: " + sb.getId() + " is successfully saved!").build();
        } else {
            return SimpleResponse.builder().status(HttpStatus.BAD_REQUEST).message("Subcategory with name: " + request.name() + " is already exist!").build();
        }

    }


    @Override
    public List<SubcategoryResponse> getByCategory(String word) {
        if (word == null) {
            return subcategoryRepository.getAllSb();
        } else {
            return subcategoryRepository.getByCategory(word);
        }
    }

    @Override
    public SubcategoryResponse getById(Long sbcId) {
        return subcategoryRepository.getSbById(sbcId).orElseThrow(() -> new NotFoundException("SubCategory with id: " + sbcId + " is no exist!"));
    }

    @Override
    public SimpleResponse update(Long sbcId, SubcategoryRequest request) {
        Subcategory sb = subcategoryRepository.findById(sbcId).orElseThrow(() -> new NotFoundException("SubCategory with id: " + sbcId + " is no exist!"));
        Category category = categoryRepository.findById(request.categoryId()).orElseThrow(() -> new NotFoundException(("Category with id: " + request.categoryId() + " is no exist!")));
        sb.setCategory(category);
        sb.setName(request.name());
        subcategoryRepository.save(sb);
        return SimpleResponse.builder().status(HttpStatus.OK).message("Subcategory with id: " + sb.getId() + " is successfully saved!").build();
    }

    @Override
    public SimpleResponse deleteById(Long sbcId) {
        subcategoryRepository.findById(sbcId).orElseThrow(() -> new NotFoundException("Subcategory with id: " + sbcId + " is no exist"));
        subcategoryRepository.deleteById(sbcId);
        return SimpleResponse.builder().status(HttpStatus.OK).message("Subcategory with id: " + sbcId + " is successfully saved!").build();
    }

    @Override
    public Map<String, List<SubcategoryResponse>> filterByCategory() {
        return subcategoryRepository.getAllSb().stream().collect(Collectors.groupingBy(SubcategoryResponse::categoryName));
    }
}
