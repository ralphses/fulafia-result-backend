package com.clicks.fulafiaresultcheckingverificationsystem.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;



@ExtendWith(SpringExtension.class)
class SessionCompareTest {

    @Test
    public void shouldCompareTwoValidSession() {

        String first = "2019/2020";
        String second = "2020/2021";
        String third = "2021/2022";
        String forth = "2022/2023";

        List<String> sessions = List.of(first, second, third, forth);

        var strings = sessions.stream().filter(s -> SessionCompare.compareSession(s, "2020/2021")).toList();

        assertEquals(strings, List.of("2020/2021", "2021/2022", "2022/2023"));

    }

    @Test
    public void shouldCompareTwoInValidSession() {

        String first = "2019/2020";
        String second = "2020/2021";
        String third = "2021/2022";
        String forth = "2022/2023";

        List<String> sessions = List.of(first, second, third, forth);

        var strings = sessions.stream().filter(s -> SessionCompare.compareSession(s, "2021/2022")).toList();

        assertNotEquals(strings, List.of("2020/2021", "2021/2022", "2022/2023"));

    }

}