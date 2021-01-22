package org.akriuchk.minishop.controller;


import lombok.RequiredArgsConstructor;
import org.akriuchk.minishop.dto.CategoryDto;
import org.akriuchk.minishop.rest.ApiResponse;
import org.akriuchk.minishop.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    @Autowired
    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAll() {
        List<CategoryDto> body = categoryService.listAll();
//        ResponseEntity.ok(body);

        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @Secured("ROLE_ADMIN")
    @PostMapping
    public ResponseEntity<ApiResponse> addCategory(@RequestBody @Valid CategoryDto categoryDto) {
        categoryService.addCategory(categoryDto);
        return new ResponseEntity<>(new ApiResponse(true, "Category has been added"), HttpStatus.CREATED);
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/{name}")
    public ResponseEntity<ApiResponse> updateCategory(@PathVariable("name") String name, @RequestBody @Valid CategoryDto category) {
        CategoryDto updateCategory = categoryService.update(name, category);
        return new ResponseEntity<>(new ApiResponse(true, "Category has been updated"), HttpStatus.OK);
    }
}
