package co.cstad.sen.api.product;

import com.github.pagehelper.PageInfo;

import java.util.UUID;

public interface ProductService {
    /**
     * use to retrieve all users from database and response with pagination
     *
     * @param page  : location page
     * @param limit : size of page
     * @return PageInfo of Tutorial is pagination
     */
    PageInfo<ProductDto> getAllProducts(int page, int limit);

    ProductDto getProductById(UUID productId);

    ProductDto saveProduct(ProductCreateDto productCreateDto);

    void deleteProduct(UUID productId);
    ProductDto updateProduct(UUID uuid, ProductDto productDto);

}
