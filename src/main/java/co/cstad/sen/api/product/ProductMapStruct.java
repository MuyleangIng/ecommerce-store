

package co.cstad.sen.api.product;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapStruct {
    ProductMapStruct INSTANCE = Mappers.getMapper(ProductMapStruct.class);

    ProductDto productToProductDto(Product product);

    @Mapping(target = "id", ignore = true)
    Product productDtoToProduct(ProductDto productDto);

    List<ProductDto> toProductDtoList(List<Product> productList);
}