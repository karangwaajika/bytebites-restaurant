package com.lab.restaurant_service.service.imp;

import com.lab.restaurant_service.dto.MenuRequestDto;
import com.lab.restaurant_service.dto.MenuResponseDto;
import com.lab.restaurant_service.exception.MenuExistsException;
import com.lab.restaurant_service.exception.MenuNotFoundException;
import com.lab.restaurant_service.model.MenuEntity;
import com.lab.restaurant_service.repository.MenuRepository;
import com.lab.restaurant_service.service.MenuService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class MenuServiceImpl implements MenuService {
    ModelMapper modelMapper;
    MenuRepository menuRepository;

    public MenuServiceImpl(ModelMapper modelMapper,
                           MenuRepository menuRepository) {
        this.modelMapper = modelMapper;
        this.menuRepository = menuRepository;
    }

    @Override
    public MenuResponseDto create(MenuRequestDto menuDto) {
        if(findByName(menuDto.getName()).isPresent()){
            throw new MenuExistsException(
                    String.format("A menu with the name '%s' already exist",
                            menuDto.getName()));
        }
        MenuEntity menu = this.modelMapper.map(menuDto, MenuEntity.class);
        menu.setId(null);
        MenuEntity savedMenu = this.menuRepository.save(menu);

        return this.modelMapper.map(savedMenu, MenuResponseDto.class);
    }

    @Override
    public Optional<MenuEntity> findByName(String name) {
        return this.menuRepository.findByName(name);
    }

    @Override
    public Optional<MenuEntity> findById(Long id) {
        return this.menuRepository.findById(id);
    }

    @Override
    public Page<MenuResponseDto> findAll(Pageable pageable) {
        Page<MenuEntity> menus = this.menuRepository.findAll(pageable);
        return menus.map(menu->this.modelMapper.map(menu, MenuResponseDto.class));
    }

    @Override
    public MenuResponseDto partialUpdate(MenuRequestDto menuRequestDto, Long menuId) {
        MenuEntity menuEntity = findById(menuId)
                .orElseThrow(() -> new MenuNotFoundException(
                        String.format("A menu with the Id '%d' doesn't exist", menuId)));

        if(menuRequestDto.getName() != null){
            menuEntity.setName(menuRequestDto.getName());
        }
        if(menuRequestDto.getPrice() != 0){
            menuEntity.setPrice(menuRequestDto.getPrice());
        }

        MenuEntity updatedMenu = this.menuRepository.save(menuEntity);

        return this.modelMapper.map(updatedMenu, MenuResponseDto.class);
    }

    @Override
    public void deleteById(Long id) {
        if(findById(id).isEmpty()){
            throw new MenuNotFoundException(
                    String.format("A menu with the Id '%d' doesn't exist", id));
        }
        this.menuRepository.deleteById(id);
    }
}
