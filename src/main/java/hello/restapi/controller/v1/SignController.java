package hello.restapi.controller.v1;

import hello.restapi.config.security.JwtTokenProvider;
import hello.restapi.controller.dto.UserDto;
import hello.restapi.entity.User;
import hello.restapi.model.response.CommonResult;
import hello.restapi.model.response.SingleResult;
import hello.restapi.model.social.KakaoProfile;
import hello.restapi.service.ResponseService;
import hello.restapi.service.UserService;
import hello.restapi.service.social.KakaoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Api(tags = {"1. Sign"})
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1")
public class SignController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final ResponseService responseService;
    private final KakaoService kakaoService;

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

    @ApiOperation(value = "소셜 로그인", notes = "소셜 회원 로그인을 한다.")
    @PostMapping(value = "/signin/{provider}")
    public SingleResult<String> signinByProvider(
            @ApiParam(value = "서비스 제공자 provider", required = true, defaultValue = "kakao") @PathVariable String provider,
            @ApiParam(value = "소셜 access_token", required = true) @RequestParam String accessToken) {
        KakaoProfile profile = kakaoService.getKakaoProfile(accessToken);
        User user = userService.findUserByProvider(profile, provider);
        return responseService.getSingleResult(jwtTokenProvider.createToken(String.valueOf(user.getUserId()), user.getRoles()));
    }

    @ApiOperation(value = "소셜 계정 가입", notes = "소셜 계정 회원가입을 한다")
    @PostMapping(value = "/signup/{provider}")
    public CommonResult signUpProvider(@ApiParam(value = "서비스 제공자 provider", required = true, defaultValue = "kakao") @PathVariable String provider,
                                       @ApiParam(value = "소셜 access_token", required = true) @RequestParam String accessToken,
                                       @ApiParam(value = "이름", required = true) @RequestParam String name) {
        KakaoProfile profile = kakaoService.getKakaoProfile(accessToken);

        userService.signUpProvider(profile, provider, name);
        return responseService.getSuccessResult();
    }
}
