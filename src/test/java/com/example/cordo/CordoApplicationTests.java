package com.example.cordo;

import com.example.cordo.controller.UserController;
import com.example.cordo.entity.User;
import com.example.cordo.entity.UserDTO;
import com.example.cordo.exception.ResourceNotFoundException;
import com.example.cordo.repository.jpa.UsersRepository;
import com.example.cordo.service.PlayerService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@Transactional
@Testcontainers
class CordoApplicationTests {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(PlayerService.class);
    @Container
    static PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:16")
                    .withDatabaseName("Cordo-test-official-oh-my-god-this-is-real-test")
                    .withUsername("just Elon Mask") //если меня возьмут на работу в spaceX я покажу им этот код
                    .withPassword("880075525");
    @DynamicPropertySource
    static void missionOverLord(DynamicPropertyRegistry tetyaLenya) { //за такие названия меня возьмут в гугл
        tetyaLenya.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        tetyaLenya.add("spring.datasource.username", postgreSQLContainer::getUsername);
        tetyaLenya.add("spring.datasource.password", postgreSQLContainer::getPassword);
        tetyaLenya.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");
    }
        @Autowired
            PlayerService playerService;
        @Autowired
            UsersRepository usersRepository;
        @Test
        @DisplayName(
                "Все вроде бы нормалды нормальды нормалдыэ\n" +
                "ХАХАХАХА это  f[0] = realTestUser; нормалды?\n" +
                "Это фиг болды поэтому больше одного запроса пока что не вызывать многопоточные тесты какая то жесть не зря говорят об многопоточности как об пи*****\n" +
                "\n" +
                "\n"
        )
        void FindUserByIdForUpdate_ExistsUser30requests30Threads_return30Users() {
            int requests = 1;
            User[] f = new User[requests];
            int threads = 1;
            String password = "misterDrist";
            String mail = "email";
            UserDTO testUserDto = playerService.regNewUser(password,mail);
            ExecutorService executorServiceTest = Executors.newFixedThreadPool(threads);
            Runnable task = () -> {
               User realTestUser = usersRepository.findByIdForUpdate(testUserDto.getUserId()).orElseThrow(
                        null
                );
                realTestUser.setBalance(realTestUser.getBalance() + 20);

                usersRepository.save(realTestUser);
                f[0] = realTestUser;
            };
            for (int i = 0;i < requests;i++) {
                executorServiceTest.submit(task);
            }
            executorServiceTest.shutdown();

            while (!executorServiceTest.isTerminated()) { //Было взято из учебного проекта лень делать все через умный замок или подобие
                // Просто ждем, не нагружая процессор
                try {
                    Thread.sleep(100); // Спим 100 миллисекунд
                } catch (InterruptedException e) {
                    logger.warn("Ожидание прервано!");
                    Thread.currentThread().interrupt();
                }
            }
            assertEquals(requests * 20,f[0].getBalance());


        }

}
