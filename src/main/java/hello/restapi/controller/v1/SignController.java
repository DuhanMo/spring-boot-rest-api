package hello.restapi.controller.v1;

import hello.restapi.config.security.JwtTokenProvider;
import hello.restapi.controller.dto.UserDto;
import hello.restapi.entity.User;
import hello.restapi.model.response.CommonResult;
import hello.restapi.model.response.SingleResult;
import hello.restapi.service.ResponseService;
import hello.restapi.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = {"1. Sign"})
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1")
public class SignController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final ResponseService responseService;

    @ApiOperation(value = "로그인", notes = "회원 로그인을 한다.")
    @PostMapping(value = "/signin")
    public SingleResult<String> singIn(@ApiParam(value = "회원 로그인 정보") @RequestBody UserDto userDto) {
        User user = userService.signIn(userDto);
        return responseService.getSingleResult(jwtTokenProvider.createToken(String.valueOf(user.getId()), user.getRoles()));
    }

    @ApiOperation(value = "가입", notes = "회원가입을 한다.")
    @PostMapping(value = "/signup")
    public CommonResult singUp(@ApiParam(value = "회원가입 정보") @RequestBody UserDto userDto) {
        userService.signUp(userDto);
        return responseService.getSuccessResult();
    }
}
