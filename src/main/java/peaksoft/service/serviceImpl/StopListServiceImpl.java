package peaksoft.service.serviceImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import peaksoft.dto.request.StopListRequest;
import peaksoft.dto.SimpleResponse;
import peaksoft.dto.response.StopListResponse;
import peaksoft.entity.MenuItem;
import peaksoft.entity.StopList;
import peaksoft.exceptions.AlreadyExistException;
import peaksoft.exceptions.NotFoundException;
import peaksoft.repository.MenuItemRepository;
import peaksoft.repository.StopListRepository;
import peaksoft.service.StopListService;
import javax.management.openmbean.KeyAlreadyExistsException;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
@RequiredArgsConstructor
public class StopListServiceImpl implements StopListService {
    private final StopListRepository stopListRepository;
    private final MenuItemRepository menuItemRepository;

    @Override
    public SimpleResponse save(StopListRequest request) {
        MenuItem menuItem = menuItemRepository.findById(request.menuItemId()).orElseThrow(() -> new NotFoundException("MenuItem is not"));
        Boolean exists = stopListRepository.existsByMenuItem(menuItem);
        if(!(menuItem.getStopList()==null)) {
            if (!exists && !menuItem.getStopList().getDate().equals(request.date())) {
                StopList stopList = new StopList();
                stopList.setDate(request.date());
                stopList.setReason(request.reason());
                stopList.setMenuItem(menuItem);
                menuItem.setStopList(stopList);
                stopListRepository.save(stopList);
                return SimpleResponse.builder().status(HttpStatus.OK).message("StopList with id:" + stopList.getId() + " is saved!").build();

            } else {
                throw new AlreadyExistException("This info already exist!");
            }
        }else {
            StopList stopList = new StopList();
            stopList.setDate(request.date());
            stopList.setReason(request.reason());
            stopList.setMenuItem(menuItem);
            menuItem.setStopList(stopList);
            stopListRepository.save(stopList);
            return SimpleResponse.builder().status(HttpStatus.OK).message("StopList with id:" + stopList.getId() + " is saved!").build();
        }
    }

    @Override
    public List<StopListResponse> getAll() {
        return stopListRepository.getAllStops();
    }

    @Override
    public StopListResponse getById(Long id) {
        return stopListRepository.getStopById(id).orElseThrow(() -> new NotFoundException("StopList with id: " + id + " is no exist!"));
    }

    @Override
    public SimpleResponse update(Long id, StopListRequest request) {
        StopList st = stopListRepository.findById(id).orElseThrow(() -> new NotFoundException("StopList with id: " + id + " is no exist!"));
        MenuItem menuItem = menuItemRepository.findById(request.menuItemId()).orElseThrow(() -> new NotFoundException("Menu item with id: " + request.menuItemId() + " is no exist!"));
        List<StopList> all = stopListRepository.findAll();
        all.remove(st);
        for (StopList stopList : all) {
            if (stopList.getMenuItem().getName().equals(menuItem.getName()) && stopList.getDate().equals(request.date())) {
                return SimpleResponse.builder().status(HttpStatus.BAD_REQUEST).message("This info already exist!").build();
            } else {
                st.setMenuItem(menuItem);
                st.setReason(request.reason());
                st.setDate(request.date());
                stopListRepository.save(st);
                return SimpleResponse.builder().status(HttpStatus.OK).message("StopList with id: " + st.getId() + " is successfully updated!").build();

            }
        }
        return null;
    }




    @Override
    @Transactional
    public SimpleResponse deleteById(Long id) {
        StopList st = stopListRepository.findById(id).orElseThrow(() -> new NotFoundException("StopList with id: "+id+" is no exist!"));
        st.getMenuItem().setInStock(true);
        stopListRepository.delete(id);
        return SimpleResponse.builder().status(HttpStatus.OK).message("StopList with id: "+st.getId()+" is successfully deleted!").build();
    }
}
