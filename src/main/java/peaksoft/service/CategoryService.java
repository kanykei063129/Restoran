package peaksoft.service;

import peaksoft.dto.SimpleResponse;
import peaksoft.dto.request.CategoryRequest;
import peaksoft.dto.response.CategoryResponse;
import peaksoft.entity.Category;

import java.util.List;

public interface CategoryService {
    SimpleResponse save(CategoryRequest category);
    List<CategoryResponse> getAll();
    CategoryResponse getById(Long id);
    SimpleResponse deleteById(Long id);
    SimpleResponse update(Long id, Category category);
}
