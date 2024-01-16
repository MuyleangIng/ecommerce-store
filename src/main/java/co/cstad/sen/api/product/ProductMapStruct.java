package co.cstad.sen.api.product;

import co.cstad.sen.api.users.User;
import co.cstad.sen.api.users.web.UserDto;
import com.github.pagehelper.PageInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapStruct {
    ProductMapStruct INSTANCE = Mappers.getMapper(ProductMapStruct.class);
    PageInfo<ProductDto> productPageToProductDtoPage(PageInfo<Product> productPageInfo);

    @Mapping(source = "uuid", target = "uuid")
    ProductDto productToProductDto(Product product);

    @Mapping(target = "uuid", ignore = true)
    Product productDtoToProduct(ProductDto productDto);

    @Mapping(target = "uuid", ignore = true)
    Product productCreateDtoToProduct(ProductCreateDto productCreateDto);

    List<ProductDto> toProductDtoList(List<Product> productList);
}
