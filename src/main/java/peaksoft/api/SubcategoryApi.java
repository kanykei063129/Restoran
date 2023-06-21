package peaksoft.api;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import peaksoft.dto.request.SubcategoryRequest;
import peaksoft.dto.SimpleResponse;
import peaksoft.dto.response.SubcategoryResponse;
import peaksoft.dto.response.UserResponse;
import peaksoft.entity.Category;
import peaksoft.entity.Subcategory;
import peaksoft.service.SubcategoryService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/subcategories")
@RequiredArgsConstructor
public class SubcategoryApi {
    private final SubcategoryService subcategoryService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public SimpleResponse save(@RequestBody @Valid SubcategoryRequest request){
        return subcategoryService.save(request);
    }

    @PermitAll
    @GetMapping("/{id}")
    public SubcategoryResponse getById(@PathVariable Long id){
        return subcategoryService.getById(id);
    }

    @PermitAll
    @GetMapping("/get")
    public List<SubcategoryResponse> get(@RequestParam(required = false) String word){
        return subcategoryService.getByCategory(word);
    }
    @PreAuthorize("hasAnyAuthority('ADMIN','CHEF')")
    @PutMapping("/{id}")
    public SimpleResponse update(@PathVariable Long id, @RequestBody @Valid SubcategoryRequest request){
        return subcategoryService.update(id,request);
    }
    @PreAuthorize("hasAnyAuthority('ADMIN','CHEF')")
    @DeleteMapping("/{id}")
    public SimpleResponse delete(@PathVariable Long id){
        return subcategoryService.deleteById(id);
    }
    @PermitAll
    @GetMapping("/grouping")
    public Map<String,List<SubcategoryResponse>> filter(){
        return subcategoryService.filterByCategory();
    }
}
