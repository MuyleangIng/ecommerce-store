package co.cstad.sen.api.users;

import co.cstad.sen.api.users.web.CreateUserDto;
import co.cstad.sen.api.users.web.ModifyUserDto;
import co.cstad.sen.api.users.web.UserDto;
import com.github.pagehelper.PageInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapStruct {
    UserMapStruct INSTANCE = Mappers.getMapper(UserMapStruct.class);

    PageInfo<UserDto> userPageToUserDtoPage(PageInfo<User> userPageInfo);
    UserDto userToUserDto(User model);

    @Mapping(target = "id", ignore = true)
    User createUserDtoToUser (CreateUserDto createUserDto);

    @Mapping(target = "id", ignore = true)
    User modifyUserDtoToUser (ModifyUserDto modifyUserDto);

    List<UserDto> toUserDtoRetrievedWithoutPageInfo(List<User> userList);

}
