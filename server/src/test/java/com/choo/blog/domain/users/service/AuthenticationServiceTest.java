package com.choo.blog.domain.users.service;

import com.choo.blog.domain.users.User;
import com.choo.blog.domain.users.dto.UserLoginData;
import com.choo.blog.domain.users.dto.UserRegistData;
import com.choo.blog.domain.users.repository.UserRepository;
import com.choo.blog.exceptions.LoginFailException;
import com.choo.blog.exceptions.PasswordNotMatchException;
import com.choo.blog.util.WebTokenUtil;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("인증관리")
@SpringBootTest
class AuthenticationServiceTest {
    private static final String EMAIL = "choo@email.com";
    private static final String PASSWORD = "choo@1234";
    private static final String NICKNAME = "choo";
    private static final LocalDate BIRTH_DATE = LocalDate.of(1995,11,18);
    private static final String DESCRIPTION = "description";

    @Autowired
    private UserService userService;

    @Autowired
    private WebTokenUtil webTokenUtil;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserRepository userRepository;

    @Nested
    @DisplayName("로그인은")
    class Describe_login{
        UserLoginData userLoginData;
        User user;

        @BeforeEach
        public void setUp(){
            user = userService.join(prepareUserRegistData(""));
        }

        @AfterEach
        public void cleanUp(){
            userRepository.deleteAll();
        }

        @Nested
        @DisplayName("로그인 정보가 주어지면")
        class Context_with_login_data{
            @BeforeEach
            public void setUp(){
                userLoginData = UserLoginData.builder()
                        .email(EMAIL)
                        .password(PASSWORD)
                        .build();
            }
            @Test
            @DisplayName("토큰을 반환한다.")
            void it_return_token(){
                String loginToken = authenticationService.login(userLoginData);

                String token = webTokenUtil.encode(user.getId());
                assertThat(loginToken).isEqualTo(token);
            }
        }

        @Nested
        @DisplayName("존재하지 않는 이메일이 주어지면")
        class Context_with_non_exist_email{
            @BeforeEach
            public void setUp(){
                userLoginData = UserLoginData.builder()
                        .email(EMAIL + "wrong")
                        .password(PASSWORD)
                        .build();
            }
            @Test
            @DisplayName("로그인에 실패했다는 예외를 던진다.")
            void it_throw_loginFailException(){
                assertThatThrownBy(() -> authenticationService.login(userLoginData))
                        .isInstanceOf(LoginFailException.class)
                        .hasMessageContaining(userLoginData.getEmail());
            }
        }

        @Nested
        @DisplayName("잘못된 비밀번호가 주어지면")
        class context_with_wrong_password{
            @BeforeEach
            public void setUp(){
                userLoginData = UserLoginData.builder()
                        .email(EMAIL)
                        .password(PASSWORD + "wrong")
                        .build();
            }
            @Test
            @DisplayName("비밀번호가 맞지 않다는 예외를 던진다.")
            void it_throw_passwordNotMatchException(){
                assertThatThrownBy(() -> authenticationService.login(userLoginData))
                        .isInstanceOf(PasswordNotMatchException.class)
                        .hasMessageContaining(userLoginData.getEmail());
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