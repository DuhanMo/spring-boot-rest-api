package hello.restapi.controller.exception;

import hello.restapi.advice.exception.CAuthenticationEntryPointException;
import hello.restapi.model.response.CommonResult;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/exception")
public class ExceptionController {

    @GetMapping("/entrypoint")
    public CommonResult entrypointException() {
        throw new CAuthenticationEntryPointException();
    }

    @GetMapping(value = "/accessdenied")
    public CommonResult accessDeniedException() {
        throw new AccessDeniedException("");
    }
}
