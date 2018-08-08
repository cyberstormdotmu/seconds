package com.ishoal.core.domain;

import com.ishoal.common.util.Streamable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Users implements Streamable<User> {

    private final List<User> users;

    private Users(List<User> users) {

        this.users = Collections.unmodifiableList(new ArrayList<>(users));
    }

    public static Users over(List<User> users) {
        return new Users(users);
    }

    public static Users over(User... users) {
        return new Users(Arrays.asList(users));
    }

    public static Builder someUsers() {

        return new Builder();
    }

    /**
     * Returns an iterator over elements of type {@code T}.
     *
     * @return an Iterator.
     */
    @Override
    public Iterator<User> iterator() {

        return users.iterator();
    }

    public int count() {

        return users.size();
    }

    public static final class Builder {
        private List<User> users;

        public Builder() {
            users = new ArrayList<>();
        }

        public Builder(Users copy) {

            this.users = copy.users;
        }

        public Builder addUser(User user) {

            users.add(user);
            return this;
        }

        public Users build() {

            return new Users(this.users);
        }
    }
}
