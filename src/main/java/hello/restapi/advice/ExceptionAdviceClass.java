package hello.restapi.advice;

import hello.restapi.advice.exception.CAuthenticationEntryPointException;
import hello.restapi.advice.exception.CSignInFailedException;
import hello.restapi.advice.exception.CUserExistException;
import hello.restapi.advice.exception.CUserNotFoundException;
import hello.restapi.model.response.CommonResult;
import hello.restapi.service.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestControllerAdvice
public class ExceptionAdviceClass {

    private final ResponseService responseService;
    private final MessageSource messageSource;

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult defaultException(HttpServletRequest request, Exception e) {
        return responseService.getFailResult(Integer.parseInt(getMessage("unKnown.code")), getMessage("unKnown.msg"));
    }

    @ExceptionHandler(CUserNotFoundException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult userNotFoundException(HttpServletRequest request, CUserNotFoundException e) {
        return responseService.getFailResult(Integer.parseInt(getMessage("userNotFound.code")), getMessage("userNotFound.msg"));
    }

    @ExceptionHandler(CSignInFailedException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult emailSignInFailed(HttpServletRequest request, CSignInFailedException e) {
        return responseService.getFailResult(Integer.parseInt(getMessage("emailSignInFailed.code")), getMessage("emailSignInFailed.msg"));
    }

    @ExceptionHandler(CAuthenticationEntryPointException.class)
    public CommonResult authenticationEntryPointException(HttpServletRequest request, CAuthenticationEntryPointException e) {
        return responseService.getFailResult(Integer.parseInt(getMessage("entryPointException.code")), getMessage("entryPointException.msg"));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public CommonResult AccessDeniedException(HttpServletRequest request, AccessDeniedException e) {
        return responseService.getFailResult(Integer.parseInt(getMessage("accessDenied.code")), getMessage("accessDenied.msg"));
    }

    @ExceptionHandler(CUserExistException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CommonResult communicationException(HttpServletRequest request, CUserExistException e) {
        return responseService.getFailResult(Integer.parseInt(getMessage("existingUser.code")), getMessage("existingUser.msg"));
    }

    private String getMessage(String code) {
        return getMessage(code, null);
    }

    private String getMessage(String code, Object[] args) {
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }
}