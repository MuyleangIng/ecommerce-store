package co.cstad.sen.api.product;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapStruct productMapStruct;

    @Override
    public PageInfo<ProductDto> getAllProducts(int page, int limit) {
        PageHelper.startPage(page, limit);
        List<Product> products = productRepository.findAll();
        return new PageInfo<>(productMapStruct.toProductDtoList(products));
    }

    @Override
    public ProductDto getProductById(UUID productId) {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        return optionalProduct.map(productMapStruct::productToProductDto).orElse(null);
    }

    @Override
    public ProductDto saveProduct(ProductCreateDto productCreateDto) {
        Product product = productMapStruct.productCreateDtoToProduct(productCreateDto);
        product.setUuid(UUID.randomUUID());
        Product savedProduct = productRepository.save(product);
        return productMapStruct.productToProductDto(savedProduct);
    }

    @Override
    public void deleteProduct(UUID productId) {
        productRepository.deleteById(productId);
    }

    @Override
    public ProductDto updateProduct(UUID uuid, ProductDto productDto) {
        Optional<Product> optionalProduct = productRepository.findById(uuid);

        if (optionalProduct.isPresent()) {
            // Retrieve the existing product
            Product existingProduct = optionalProduct.get();

            // Update fields with values from productDto
            existingProduct.setTitle(productDto.title()); // Accessing field directly
            existingProduct.setDescription(productDto.description()); // Accessing field directly
            existingProduct.setCover(productDto.cover());
            existingProduct.setStatus(productDto.status());
            existingProduct.setStartDate(productDto.startDate());
            existingProduct.setEndDate(productDto.endDate());

            // Save the updated product
            Product updatedProduct = productRepository.save(existingProduct);
            return productMapStruct.productToProductDto(updatedProduct);
        } else {
            return null; // Product with the specified UUID not found
        }
    }



}