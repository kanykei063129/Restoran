package peaksoft.service;

import peaksoft.dto.SimpleResponse;
import peaksoft.dto.request.SubcategoryRequest;
import peaksoft.dto.response.SubcategoryResponse;

import java.util.List;
import java.util.Map;

public interface SubcategoryService {
    SimpleResponse save(SubcategoryRequest request);
    List<SubcategoryResponse> getByCategory(String word);
    SubcategoryResponse getById(Long sbcId);
    SimpleResponse update(Long sbcId, SubcategoryRequest request);
    SimpleResponse deleteById(Long sbcId);
    Map<String, List<SubcategoryResponse>> filterByCategory();
}
