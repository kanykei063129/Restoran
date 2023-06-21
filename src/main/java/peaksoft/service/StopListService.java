package peaksoft.service;

import peaksoft.dto.SimpleResponse;
import peaksoft.dto.request.StopListRequest;
import peaksoft.dto.response.StopListResponse;

import java.util.List;

public interface StopListService {
    SimpleResponse save(StopListRequest request);
    List<StopListResponse> getAll();
    StopListResponse getById(Long id);
    SimpleResponse update(Long id, StopListRequest request);
    SimpleResponse deleteById(Long id);
}
