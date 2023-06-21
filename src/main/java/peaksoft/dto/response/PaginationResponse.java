package peaksoft.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class PaginationResponse {
        private List<MenuItemResponse> menuItemList;
        private int currentPage;
        private int pageSize;
        }
