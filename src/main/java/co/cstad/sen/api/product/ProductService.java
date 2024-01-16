package co.cstad.sen.api.product;

import java.util.List;

public interface ProductService {
    List<ProductDto> getAllProducts();

    ProductDto getProductById(Long productId);

    ProductDto saveProduct(ProductDto productDto);

    void deleteProduct(Long productId);
    ProductDto updateProduct(Long id, ProductDto productDto);

}
