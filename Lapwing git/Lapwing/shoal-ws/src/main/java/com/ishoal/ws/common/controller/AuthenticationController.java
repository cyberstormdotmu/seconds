package com.ishoal.ws.common.controller;

import com.ishoal.core.domain.Role;
import com.ishoal.core.domain.User;
import com.ishoal.ws.common.dto.LoginResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.CookieClearingLogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.ishoal.ws.common.dto.LoginResponseDto.aLoginResponse;

@RestController
@RequestMapping("/ws/")
public class AuthenticationController {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    @RequestMapping(value = "login", method = RequestMethod.GET)
    public LoginResponseDto login(User currentUser) {
        logger.info("User attempt to login: [{}]", currentUser != null ? currentUser.getUsername() : "<unknown>");
        if(currentUser == null) {
        	System.out.println(currentUser);
            throw new BadCredentialsException("Bad credentials");
        }

        LoginResponseDto.Builder response = aLoginResponse()
                .username(currentUser.getUsername())
                .activated(isActivated(currentUser));

        for(Role role : currentUser.getRoles()) {
            response.role(role.getName());
        }
        return response.build();
    }

    @RequestMapping(value = "logout", method = RequestMethod.POST)
    public void logoutPage (HttpServletRequest request, HttpServletResponse response, Authentication auth) {
        if (auth != null) {
            logger.info("User attempt to logout: [{}]", auth.getName());
            new SecurityContextLogoutHandler().logout(request, response, auth);
            new CookieClearingLogoutHandler("SESSION", "JSESSIONID", "XSRF-TOKEN", "X-XSRF-TOKEN")
                .logout(request, response, auth);
        }
    }

    // Temporary test until we add an actual flag to the User
    private boolean isActivated(User user) {
        return user.getRoles().iterator().hasNext();
    }
}
