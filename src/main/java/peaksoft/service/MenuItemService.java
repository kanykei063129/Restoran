package peaksoft.service;

import peaksoft.dto.SimpleResponse;
import peaksoft.dto.request.MenuItemRequest;
import peaksoft.dto.response.MenuItemResponse;
import peaksoft.dto.response.PaginationResponse;

import java.util.List;
import java.util.Map;

public interface MenuItemService {
    SimpleResponse save(MenuItemRequest request);
    List<MenuItemResponse> getAll();
    MenuItemResponse getById(Long menuId);
    SimpleResponse update(Long menuId, MenuItemRequest request);
    SimpleResponse deleteById(Long menuId);
    List<MenuItemResponse> globalSearch(String word);
    List<MenuItemResponse> sortByPrice(String word);
    Map<Boolean, List<MenuItemResponse>> filterByVegetarian();
    PaginationResponse getItemPagination(int page, int size);
}
