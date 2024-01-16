package co.cstad.sen.api.product.web;

import co.cstad.sen.api.product.ProductCreateDto;
import co.cstad.sen.api.product.ProductDto;
import co.cstad.sen.api.product.ProductService;
import co.cstad.sen.base.BaseApi;
import com.github.pagehelper.PageInfo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

@GetMapping
public ResponseEntity<PageInfo<ProductDto>> list(@RequestParam(defaultValue = "1") int page,
                                                 @RequestParam(defaultValue = "10") int limit) {
    PageInfo<ProductDto> products = productService.getAllProducts(page, limit);
    return  ResponseEntity.ok(products);
}

    @GetMapping("/{uuid}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable UUID uuid) {
        ProductDto productDto = productService.getProductById(uuid);
        if (productDto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(productDto);
    }
    @PostMapping
    public BaseApi<?> saveProduct(@RequestBody @Valid ProductCreateDto productCreateDto) {
        ProductDto productCreateDto1 = productService.saveProduct(productCreateDto);
        return BaseApi.builder().code(200).message("Product have been create Successfully").data(productCreateDto1).build();
    }
    @DeleteMapping("/{uuid}")
    public BaseApi<?> deleteProduct(@PathVariable UUID uuid) {
        productService.deleteProduct(uuid);
        return BaseApi.builder().code(200).message("Product have been delete Successfully").build();
    }
    @PutMapping("/{uuid}")
    public BaseApi<?> updateProduct(@PathVariable UUID uuid, @RequestBody @Valid ProductDto productDto) {
        ProductDto updatedProductDto = productService.updateProduct(uuid, productDto);
        if (updatedProductDto == null) {
            return BaseApi.builder().code(404).message("Product not found").build();
        }
        return BaseApi.builder().code(200).message("Product have been update Successfully").data(updatedProductDto).build();
    }
}