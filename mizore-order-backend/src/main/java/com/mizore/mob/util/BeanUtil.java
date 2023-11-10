package com.mizore.mob.util;

import jakarta.validation.constraints.NotNull;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class BeanUtil extends cn.hutool.core.bean.BeanUtil {

    public static void copyNotNullProperties(Object source, @NotNull Object target) {
        // 将source的非null属性覆盖到target
        Field[] fields = source.getClass().getDeclaredFields();
        List<String> ignoreProperties = new ArrayList<>();
        for (Field f : fields) {
            f.setAccessible(true);
            try {
                Object value = f.get(source);
                if (value == null) {
                    ignoreProperties.add(f.getName());
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        String[] ip = ignoreProperties.toArray(new String[ignoreProperties.size()]);
        copyProperties(source, target, ip);
    }
}
