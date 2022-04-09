package com.choo.blog.domain.users;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("회원 관리")
class UserServiceTest {
    private static final String EMAIL = "choo@email.com";
    private static final String PASSWORD = "password";
    private static final String NICKNAME = "choo";
    private static final LocalDate BIRTH_DATE = LocalDate.of(1995,11,18);
    private static final String DESCRIPTION = "description";
    @Nested
    @DisplayName("회원 가입은")
    class Descrive_join{
        @Autowired
        UserService userService;

        @Nested
        @DisplayName("회원 정보를 입력받으면")
        class context_with_user_info{
            UserRegistData registData;

            @BeforeEach
            void setUp(){
                registData = prepareUserRegistData("");
            }

            @Test
            @DisplayName("회원을 등록하고 등록된 회원 정보를 반환한다.")
            void it_return_user(){
                User user = userService.join(registData);

                assertThat(user.getEmail()).isEqualTo(registData.getEmail());
                assertThat(user.getBirthdate()).isEqualTo(registData.getBirthdate());
                assertThat(user.getDescription()).isEqualTo(registData.getDescription());
                assertThat(user.getNickname()).isEqualTo(registData.getNickname());
            }
        }
    }

    public UserRegistData prepareUserRegistData(String suffix){
        return UserRegistData.builder()
                .email(EMAIL)
                .password(PASSWORD)
                .nickname(NICKNAME)
                .birthdate(BIRTH_DATE)
                .description(DESCRIPTION)
                .build();
    }
}