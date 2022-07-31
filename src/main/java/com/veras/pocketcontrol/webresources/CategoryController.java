package com.veras.pocketcontrol.webresources;

import com.veras.pocketcontrol.models.Category;
import com.veras.pocketcontrol.services.CategoryService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/categories")
@AllArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;


    @GetMapping
    @ApiOperation(value = "", authorizations = { @Authorization(value="jwtToken") })
    public ResponseEntity<List<Category>> fetchCategory(@RequestParam(required = false) String id) {
        Optional<List<Category>> category = id == null ? categoryService.getAllCategories() : Optional.of(categoryService.getCategory(id).stream().toList());
        if(category.isPresent()){
            return ResponseEntity.of(category);
        } else {
            return ResponseEntity.of(Optional.of(Collections.emptyList()));
        }
    }

    @PostMapping
    @ApiOperation(value = "", authorizations = { @Authorization(value="jwtToken") })
    public Category insertCategory(@RequestBody Category category){
        return categoryService.insertCategory(category);
    }

    @PutMapping
    @ApiOperation(value = "", authorizations = { @Authorization(value="jwtToken") })
    public Category updateCategory(@RequestBody Category category){
        return categoryService.updateCategory(category);
    }

    @DeleteMapping
    @ApiOperation(value = "", authorizations = { @Authorization(value="jwtToken") })
    public Category deleteCategory(@RequestParam String id) {
        return categoryService.deleteCategory(id);
    }
}
