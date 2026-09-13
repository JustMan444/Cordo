package com.example.cordo;
import com.example.cordo.controller.UserController;
import com.example.cordo.entity.User;
import com.example.cordo.entity.UserDTO;
import com.example.cordo.exception.BalanceLimitExceededException;
import com.example.cordo.repository.jpa.UsersRepository;
import com.example.cordo.service.BaseSecurity;
import com.example.cordo.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import org.springframework.http.MediaType;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class Mini_Tests {
    @Mock
    BaseSecurity baseSecurity;
    @Mock
    UsersRepository usersRepository;
    @InjectMocks
    PlayerService playerService;

    @Test
    void save_existsUser_saveUserAndReturn() {
        when(baseSecurity.encodePassword(anyString())).thenReturn("ENCODED");
         User user1 = new User(baseSecurity.encodePassword("I AM MISTER BEAST!!!"),"880075525@mail.ru");
        when(usersRepository.save(any(User.class)))
                .thenReturn(user1);
       UserDTO user =  playerService.regNewUser("I AM MISTER BEAST!!!","880075525@mail.ru");
        assertEquals(new UserDTO(new User("ENCODED", "880075525@mail.ru")).getUserId(),user.getUserId());
        assertEquals("880075525@mail.ru",user.getEmail());
        assertEquals(new UserDTO(new User("ENCODED", "880075525@mail.ru")).getBalance(),user.getBalance());
        verify(usersRepository,times(1)).save(any(User.class));
        verify(baseSecurity,times(2)).encodePassword(anyString());
    }

}
    class Тыгындыш {
        private static final org.slf4j.Logger logger = LoggerFactory.getLogger(PlayerService.class);
        @Test
        void topUpBalance_notExistsUser_throwIllegalArgumentException() {
            User fakeUser = null; //new User("FAKE_USER444","I don't have email  because I am fake");
            int fakeMoney = 200;
            PlayerService playerService = new PlayerService(
                    null,
                    null,
                    null,
                    null,
                    255
            );

            Exception ex = assertThrows(IllegalArgumentException.class,
                    () -> playerService.topOpBalance(fakeUser,fakeMoney));
         //   playerService.topOpBalance(fakeUser,fakeMoney);


            assertEquals("User cannot be null",ex.getMessage());

        }
        @Test
        void topUpBalanceLimitException_ExistsUser_ThrowBalanceLimitExceededException() {
            User fakeUser = new User("Elon Mask","I don't have email  because i am billionaire");
            int fakeMoney = 2_100_000_000;
            logger.warn("ВСЕ ЖЕ КАКОЙ Я ДАУН КОГДА ЗАБЫЛ ДОПИСАТЬ 2 ЧЕРТОВЫХ НОЛИКА К ПОПОЛНЕНИЮ");
            PlayerService playerService = new PlayerService(
                    null,
                    null,
                    null,
                    null,
                    (int) 2e9
            );

            Exception ex = assertThrows(BalanceLimitExceededException.class,
                    () -> playerService.topOpBalance(fakeUser,fakeMoney));

            assertEquals("Balance limit exceeded",ex.getMessage());
        }
    }
    @WebMvcTest(UserController.class)
    class UserControllerTest {
        private static final org.slf4j.Logger logger = LoggerFactory.getLogger(PlayerService.class);
    @Autowired
        MockMvc mockMvc;
    @MockitoBean
    PlayerService playerService;
    @Test
        void registering_returnUserDto() throws Exception{
        logger.warn("ПЕРЕД СТАРТОМ ОТКЛЮЧИТЕ Редис и JPA репозитории под корень они нахуй взорвут тест не потому что у меня руки из жопы а потому что у некоторых разработчиков спринга");
        when(playerService.regNewUser(anyString(),anyString()))
                .thenReturn(new UserDTO(new User("Где деньги взять?Давно известно...", "CommandTesters@mail.ru")));
        mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                        "email": "CommandTesters@mail.ru",
                        "password": "Its test!"
                        }
                        """)

        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value("0"))
                .andExpect(jsonPath("$.email").value("CommandTesters@mail.ru"));


    }
    }
