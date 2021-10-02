package hello.restapi.controller.v1;

import hello.restapi.controller.dto.UserDto;
import hello.restapi.entity.User;
import hello.restapi.model.response.CommonResult;
import hello.restapi.model.response.ListResult;
import hello.restapi.model.response.SingleResult;
import hello.restapi.service.ResponseService;
import hello.restapi.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = {"1. User@@"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class UserController {

    private final UserService userService;
    private final ResponseService responseService;

    @ApiOperation(value = "회원 리스트 조회", notes = "모든 회원을 조회한다")
    @GetMapping("/users")
    public ListResult<User> findAllUser() {
        return responseService.getListResult(userService.findAllUser());
    }

    @ApiOperation(value = "회원 단건 조회", notes = "특정 회원을 조회한다")
    @GetMapping("/user/{userId}")
    public SingleResult<User> findUser(@ApiParam(value = "회원 아이디", required = true) @PathVariable String userId) {
        return responseService.getSingleResult(userService.findUser(userId));
    }

    @ApiOperation(value = "회원 입력", notes = "회원을 입력한다")
    @PostMapping("/user")
    public SingleResult<User> saveUser(@ApiParam(value = "회원정보", required = true) @RequestBody UserDto userDto) {
        return responseService.getSingleResult(userService.saveUser(userDto));
    }

    @ApiOperation(value = "회원 수정", notes = "회원정보를 수정한다")
    @PutMapping("/user")
    public SingleResult<User> modifyUser(@ApiParam(value = "회원정보", required = true) @RequestBody UserDto userDto) {
        return responseService.getSingleResult(userService.modifyUser(userDto));
    }

    @ApiOperation(value = "회원 삭제", notes = "회원을 삭제한다.")
    @DeleteMapping("/user/{userId}")
    public CommonResult deleteUser(@ApiParam(value = "회원 아이디", required = true) @PathVariable String userId) {
        userService.deleteUser(userId);
        return responseService.getSuccessResult();
    }
}
