package co.cstad.sen.api.product;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapStruct productMapStruct;

    @Override
    public List<ProductDto> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return productMapStruct.toProductDtoList(products);
    }

    @Override
    public ProductDto saveProduct(ProductDto productDto) {
        Product product = productMapStruct.productDtoToProduct(productDto);
        Product savedProduct = productRepository.save(product);
        return productMapStruct.productToProductDto(savedProduct);
    }

    @Override
    public ProductDto getProductById(Long productId) {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        return optionalProduct.map(productMapStruct::productToProductDto).orElse(null);
    }

    @Override
    public void deleteProduct(Long productId) {
        productRepository.deleteById(productId);
    }
    @Override
    public ProductDto updateProduct(Long id, ProductDto productDto) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()) {
            Product product = productMapStruct.productDtoToProduct(productDto);
            product.setId(id); // Ensure the ID is not changed
            Product updatedProduct = productRepository.save(product);
            return productMapStruct.productToProductDto(updatedProduct);
        } else {
            return null;
        }
    }

}