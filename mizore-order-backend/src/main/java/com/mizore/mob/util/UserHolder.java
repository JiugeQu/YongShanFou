package com.mizore.mob.util;

import com.mizore.mob.entity.User;

public class UserHolder {
    private static ThreadLocal<User> tl = new ThreadLocal();

    public static void save(User user) {
        tl.set(user);
    }

    public static User get() {
        return tl.get();
    }

    public static void remove() {
        tl.remove();
    }

}
