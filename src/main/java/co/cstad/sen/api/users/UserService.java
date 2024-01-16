package co.cstad.sen.api.users;

import co.cstad.sen.api.users.web.CreateUserDto;
import co.cstad.sen.api.users.web.ModifyUserDto;
import co.cstad.sen.api.users.web.UserDto;
import com.github.pagehelper.PageInfo;


public interface UserService {

    /**
     * use to retrieve all users from database and response with pagination
     *
     * @param page  : location page
     * @param limit : size of page
     * @return PageInfo of Tutorial is pagination
     */
    PageInfo<UserDto> getAllUsers(int page, int limit);

//    /**
//     * select user by uuid
//     *
//     * @param uuid : needed id in order to do the searching
//     * @return UserDto define response data
//     */
//    UserDto getUserByUuid(String uuid);

    /**
     * insert users
     *
     * @param createUserDto : is data need to insert and require
     * @return ModifyDto define response data
     */
    UserDto createUser(CreateUserDto createUserDto);

    /**
     * update user
     *
     * @param modifyUserDto : data need to update
     * @return :ModifyUserDto use to response that necessary to response
     */
    UserDto updateUser(ModifyUserDto modifyUserDto, String id);

    /**
     * use to delete user by uuid, and it just updates status from false to true
     *
     * @param id is required to search before delete
     */
    void deleteUserById(Long id);

    UserDto getById(Long id);

}
