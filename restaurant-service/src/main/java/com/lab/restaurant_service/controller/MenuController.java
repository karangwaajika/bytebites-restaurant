package com.lab.restaurant_service.controller;

import com.lab.restaurant_service.dto.MenuDto;
import com.lab.restaurant_service.dto.MenuRequestDto;
import com.lab.restaurant_service.dto.MenuResponseDto;
import com.lab.restaurant_service.service.MenuService;
import io.swagger.v3.oas.annotations.Operation;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("menu")
public class MenuController {
    ModelMapper modelMapper;
    MenuService menuService;

    public MenuController(ModelMapper modelMapper,
                          MenuService menuService) {
        this.modelMapper = modelMapper;
        this.menuService = menuService;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'RESTAURANT_OWNER')")
    @PostMapping("/add")
    @Operation(summary = "Add menu",
            description = "This request inserts a menu to the database and returns " +
                          "the inserted menu ")
    public ResponseEntity<MenuResponseDto> registerUser(
            @RequestBody MenuRequestDto menuRequestDto
    ) {
        MenuResponseDto savedUser = this.menuService.create(menuRequestDto);
        return ResponseEntity.ok(savedUser);
    }

    @GetMapping(name = "view_menus", path = "/view")
    @Operation(summary = "View menus",
            description = "This method applies pagination for efficient retrieval " +
                          "of menus list")
    public Page<MenuResponseDto> viewUsers(Pageable pageable){
        return this.menuService.findAll(pageable);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'RESTAURANT_OWNER')")
    @DeleteMapping(name = "delete_menu", path = "/delete")
    @Operation(summary = "Delete Menu",
            description = "The menu is delete using its id that is retrieved " +
                          "as a query parameter from the url")
    public ResponseEntity<?> deleteMenu(@RequestParam Long id){
        this.menuService.deleteById(id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(Map.of("message", "Menu deleted successfully"));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'RESTAURANT_OWNER')")
    @PatchMapping(name = "update_menu", path = "/update")
    @Operation(summary = "Update Menu",
            description = "The menu can be updated partially, " +
                          "it's doesn't necessary required " +
                          "all the fields to be updated")
    public ResponseEntity<MenuResponseDto> updateMenu(@RequestBody MenuRequestDto menuDto,
                                                      @RequestParam Long menuId){

        MenuResponseDto updatedMenu = this.menuService.partialUpdate(menuDto, menuId);

        return ResponseEntity.status(HttpStatus.OK).body(updatedMenu);
    }

    //    for internal communication between services
    @GetMapping(name = "find_menu_by_id", path = "/view/{id}")
    @Operation(summary = "Find Menu",
            description = "Search and view only one menu using menu ID")
    public MenuDto viewMenu(@PathVariable Long id){

        return this.modelMapper.map(this.menuService.findById(id), MenuDto.class);
    }
}
