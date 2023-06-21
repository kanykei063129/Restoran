package peaksoft.service.serviceImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import peaksoft.dto.request.MenuItemRequest;
import peaksoft.dto.response.MenuItemResponse;
import peaksoft.dto.response.PaginationResponse;
import peaksoft.dto.SimpleResponse;
import peaksoft.entity.MenuItem;
import peaksoft.entity.Restaurant;
import peaksoft.entity.StopList;
import peaksoft.entity.Subcategory;
import peaksoft.exceptions.NotFoundException;
import peaksoft.repository.MenuItemRepository;
import peaksoft.repository.RestaurantRepository;
import peaksoft.repository.StopListRepository;
import peaksoft.repository.SubcategoryRepository;
import peaksoft.service.MenuItemService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class MenuItemServiceImpl implements MenuItemService {
    private final MenuItemRepository menuItemRepository;
    private final RestaurantRepository restaurantRepository;
    private final SubcategoryRepository subcategoryRepository;
    private final StopListRepository stopListRepository;

    @Override
    public SimpleResponse save(MenuItemRequest request) {
        if (menuItemRepository.existsByName(request.name())) {
            return SimpleResponse.builder().status(HttpStatus.BAD_REQUEST).message("Menu item with name: " + request.name() + " is already exist!").build();
        } else {
            Restaurant restaurant = restaurantRepository.findById(request.restaurantId()).orElseThrow(() -> new NotFoundException("Restaurant with id: " + request.restaurantId() + " is no exist!"));
            Subcategory subcategory = subcategoryRepository.findById(request.subcategoryId()).orElseThrow(() -> new NotFoundException("Subcategory with id: " + request.subcategoryId() + " is no exist!"));
            MenuItem menuItem = new MenuItem();
            menuItem.setRestaurant(restaurant);
            menuItem.setSubcategory(subcategory);
            menuItem.setName(request.name());
            menuItem.setImage(request.image());
            menuItem.setPrice(request.price());
            menuItem.setDescription(request.description());
            menuItem.setVegetarian(request.isVegetarian());
            menuItem.setInStock(true);
            menuItemRepository.save(menuItem);
            return SimpleResponse.builder().status(HttpStatus.OK).message("MenuItem with id: " + menuItem.getId() + " is saved!").build();
        }
    }

    @Override
    public List<MenuItemResponse> getAll() {
        return menuItemRepository.getAllMenus();

    }

    @Override
    public MenuItemResponse getById(Long menuId) {
        return menuItemRepository.getMenuById(menuId).orElseThrow(() -> new NotFoundException("Menu with id: " + menuId + " is no exist!"));

    }

    @Override
    public SimpleResponse update(Long menuId, MenuItemRequest request) {
        MenuItem menuItem = menuItemRepository.findById(menuId).orElseThrow(() -> new NotFoundException("Menu with id: " + menuId + " is no exist!"));
        menuItem.setName(request.name());
        menuItem.setImage(request.image());
        menuItem.setPrice(request.price());
        menuItem.setDescription(request.description());
        menuItem.setVegetarian(request.isVegetarian());
        menuItemRepository.save(menuItem);
        return SimpleResponse.builder().status(HttpStatus.OK).message("Menu with id: " + menuId + " is updated!").build();
    }

    @Override
    public SimpleResponse deleteById(Long menuId) {
        menuItemRepository.findById(menuId).orElseThrow(() -> new NotFoundException("Menu item with id: " + menuId + " is no exist"));
        menuItemRepository.deleteById(menuId);
        return SimpleResponse.builder().status(HttpStatus.OK).message("Menu with id: " + menuId + " is deleted!").build();
    }

    @Override
    public List<MenuItemResponse> globalSearch(String word) {
        LocalDate currentDate = LocalDate.now();

        if (word.equalsIgnoreCase("getAll")) {
            for (StopList stop : stopListRepository.findAll()) {
                if (stop.getDate().equals(currentDate)) {
                    stop.getMenuItem().setInStock(false);
                    stopListRepository.save(stop);
                } else {
                    stop.getMenuItem().setInStock(true);
                    stopListRepository.save(stop);
                }
            }
            return menuItemRepository.getAllMenus();
        } else {
            return menuItemRepository.globalSearch(word);
        }
    }


    @Override
    public List<MenuItemResponse> sortByPrice(String word) {
        if (word.equalsIgnoreCase("Asc")) {
            return menuItemRepository.sortByPriceAsc();
        } else if (word.equalsIgnoreCase("Desc")) {
            return menuItemRepository.sortByPriceDesc();
        } else {
            return menuItemRepository.getAllMenus();
        }
    }

    @Override
    public Map<Boolean, List<MenuItemResponse>> filterByVegetarian() {
        return menuItemRepository.getAllMenus().stream().collect(Collectors.groupingBy(MenuItemResponse::IsVegetarian));
    }

    @Override
    public PaginationResponse getItemPagination(int page, int size) {
        Pageable pageable = PageRequest.of(page-1,size);
        Page<MenuItemResponse> pagedItems = menuItemRepository.findAllBy(pageable);
        List<MenuItemResponse> paged = pagedItems.getContent().stream().map(menuItem->new MenuItemResponse(
                menuItem.id(),menuItem.name(),menuItem.image(),menuItem.price(),menuItem.description(),menuItem.IsVegetarian())).toList();

        PaginationResponse menuResponse = new PaginationResponse();
        menuResponse.setMenuItemList(paged);
        menuResponse.setCurrentPage(pageable.getPageNumber());
        menuResponse.setPageSize(pagedItems.getTotalPages());
        return menuResponse;
    }
}