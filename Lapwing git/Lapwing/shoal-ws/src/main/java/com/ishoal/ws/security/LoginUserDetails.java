package com.ishoal.ws.security;

import com.ishoal.core.security.SecurePassword;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class LoginUserDetails extends User {
    private static final long serialVersionUID = 1L;

    private long userId;
    private String  username;

    public LoginUserDetails(long userId, String username, SecurePassword password,Collection<GrantedAuthority> authorities) {
        super(username, password.getValue(), authorities);
        this.userId = userId;
    }

    public long getUserId() {
        return userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }

        LoginUserDetails that = (LoginUserDetails) o;

        return userId == that.userId;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (int) (userId ^ (userId >>> 32));
        return result;
    }
}
