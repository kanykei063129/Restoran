package peaksoft.service;

import peaksoft.dto.SimpleResponse;
import peaksoft.dto.request.ChequeRequest;
import peaksoft.dto.response.ChequeResponse;

import java.util.List;

public interface ChequeService {
    SimpleResponse save(ChequeRequest request);
    List<ChequeResponse> getAll();
    ChequeResponse getById(Long id);
    SimpleResponse update(Long id, ChequeRequest request);
    SimpleResponse deleteById(Long id);
    Double getAllChequesByUser(Long userId);
    Double getAverageSum(Long restId);
}
