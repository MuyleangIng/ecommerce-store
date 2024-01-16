package co.cstad.sen.api.users.web;

import co.cstad.sen.api.users.User;
import co.cstad.sen.api.users.UserRepository;
import co.cstad.sen.api.users.UserService;
import co.cstad.sen.utils.EncryptionUtil;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@Slf4j
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
@ApiResponses(value = {
        @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(hidden = true))),
        @ApiResponse(responseCode = "405", content = @Content(schema = @Schema(hidden = true))),
})
@Tag(name = "Admin User")
@SecurityRequirement(name = "accessToken")
public class UserRestController {
    private final UserRepository userRepository;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> list() {
        List<User> users = userRepository.findAll();
        return new ResponseEntity<>(users,HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable String id) {
        id = EncryptionUtil.decrypt(id,User.getClassName());
        User user = userRepository.findById(Long.parseLong(id)).orElse(null);
        return new ResponseEntity<>(user,HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody @Valid CreateUserDto createUserDto){
        UserDto userDto = userService.createUser(createUserDto);
        return new ResponseEntity<>(userDto,HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@RequestBody @Valid ModifyUserDto modifyUserDto,@PathVariable String id){
        id = EncryptionUtil.decrypt(id,User.getClassName());
        UserDto user = userService.updateUser(modifyUserDto,id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id){
        log.info(id," id log {}");
        id = EncryptionUtil.decrypt(id,User.getClassName());
        userService.deleteUserById(Long.valueOf(id));
        return new ResponseEntity<>(id,HttpStatus.OK);
    }
}
