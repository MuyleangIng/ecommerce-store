package co.cstad.sen.api.users;

import co.cstad.sen.api.role.Role;
import co.cstad.sen.api.role.RoleRepository;
import co.cstad.sen.api.users.web.CreateUserDto;
import co.cstad.sen.api.users.web.ModifyUserDto;
import co.cstad.sen.api.users.web.UserDto;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserMapStruct userMapStruct;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private PasswordEncoder encoder;
    @Autowired
    void setEncoder(PasswordEncoder encoder){
        this.encoder =encoder;
    }

    @Override
    public PageInfo<UserDto> getAllUsers(int page, int limit) {
        PageInfo<User> userPageInfo = PageHelper.startPage(page, limit).doSelectPageInfo(userRepository::findAll);
        return userMapStruct.userPageToUserDtoPage(userPageInfo);
    }


    @Override
    public UserDto createUser(CreateUserDto createUserDto) {
        Role role= roleRepository.findById(Long.valueOf(createUserDto.roleId())).orElseThrow();
        User user = new User(createUserDto.firstName(), createUserDto.lastName(), createUserDto.username(),
                createUserDto.email(), encoder.encode(createUserDto.password()));
        user.setRoles(List.of(role));
        user.setVerified(new Timestamp(System.currentTimeMillis()).toLocalDateTime());
        user.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        user.setIsDisabled(false);

        User savedUser = userRepository.save(user);
        return userMapStruct.userToUserDto(savedUser);
    }

    @Override
    public UserDto updateUser(ModifyUserDto modifyUserDto, String id) {
        Role role = roleRepository.findById(Long.valueOf(modifyUserDto.roleId())).orElseThrow();
        User user = userRepository.findById(Long.valueOf(id)).orElseThrow();
        user.setRoles(List.of(role));
        user.setEmail(modifyUserDto.email());
        user.setPassword(encoder.encode(modifyUserDto.password()));
        user.setVerified(new Timestamp(System.currentTimeMillis()).toLocalDateTime());
        user.setFirstName(modifyUserDto.firstName());
        user.setLastName(modifyUserDto.lastName());
        user.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        user.setUsername(modifyUserDto.username());
        user.setAvatar(modifyUserDto.avatar());
        User profileUser = userRepository.save(user);
        return userMapStruct.userToUserDto(profileUser);
    }

    @Override
    public void deleteUserById(Long id) {
        if (getById(id) != null) {
            userRepository.deleteById(id);
        }
    }

    @Override
    public UserDto getById(Long id) {
        User userId = userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND
                , "user with %d not found"));
        return UserMapStruct.INSTANCE.userToUserDto(userId);
    }
}
