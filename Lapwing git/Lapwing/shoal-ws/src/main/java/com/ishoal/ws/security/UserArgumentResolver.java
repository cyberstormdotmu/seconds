package com.ishoal.ws.security;

import com.ishoal.core.domain.User;
import com.ishoal.core.user.UserService;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class UserArgumentResolver implements HandlerMethodArgumentResolver {

    private final UserService userService;

    public UserArgumentResolver(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.getParameterType().equals(User.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || !LoginUserDetails.class.isAssignableFrom(authentication.getPrincipal().getClass())) {
            return null;
        }
        LoginUserDetails userDetails = (LoginUserDetails)authentication.getPrincipal();
        User user = userService.findByUsernameIgnoreCase(userDetails.getUsername());
        user.clearPassword();
        return user;
    }
}
