
//стало легаси ведь это было обучение
package com.example.cordo.config;
//Начинаю обьяснять код ведь это лучший способ учится и запомнить комментарии ВРЕМЕННЫЕ

//Импорты
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration //сообщаем спрингу то что это конфигуриционый файл иначе он проигнорирует его (он создаст бины на все приложение)
@EnableWebSecurity //включаем веб безопастность ведь с этого момента мы остались одни без авто конфигурации
public class MySecurityConfig {
    final  JwtAuthFilter jwtAuthFilter; //создаем и делаем иньекцию через конструктор это нужно для JWT токена
    public MySecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }
    @Bean //первый бин и он довольно важен мы настраиваем цепочку фильтров ведь мы отключили авто конфигурацию
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity
                .csrf(csrf -> csrf.disable()) //сносим защиту от csrf атак ведь у нас Rest API и еще и JWT токены есть!
                .authorizeHttpRequests(auth -> auth //настраиваем кому доступен каждый эндпоинт
                        .requestMatchers("/api/v1/auth/login","/api/v1/users","/error").permitAll() //разрешаем иначе никто не зайдет на сайт PS: /error сделали открытым чтобы ошибки были понятными а не 401 я изучил эту херь
                        //если не авторизованый челик получит условную 404 тот получит 401 и хрен тот разберется что не так короче сложно но плюс /error нету там нечего криминального
                        .requestMatchers("/api/v1/subscriptions/subscribe").hasRole("ADMIN") //это теставая херь в будущем строку уберу но пока осталась чтобы посмотреть как разрешить эндпоинт конкретно для одного человека
                        //в будущем сделаем красивый сайт и RestAPI для админов будет админ панель!
                        .anyRequest().authenticated() //каждый запрос требует антуфикации (тьфу хер выговоришь) это значит что остальные эндпоинты под защитой
                )
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) //мы нахер посылаем спринга с его любимыми ссесиями ведь мало того что это говно для Rest API
                //это не безопасно и у нас есть Rest API режим Stateless значит что спринг не будет создавать сессии ВООБЩЕ и не будет использовать уже существующиеся (господи программировать более менее учусь а русский в сторонке стоит аж страшно стало)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class); //это говно по факту это добавление КАСТОМНОГО фильтра перед спринговым крутая вещь с этим надо повозится побольше
        return httpSecurity.build(); //создаем бин
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{ //бин для менеджера все тоже сложновато но есть стандартный так что пофиг
        return  authenticationConfiguration.getAuthenticationManager(); //создаем бин
    }
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() { //раньше мы не писали этого но теперь мы снесли стандартную защиту спринга и этот код спринг генерировал под капотом теперь пишем РУЧКАМИ чтобы приложение запустилось
        return new BCryptPasswordEncoder(); //создаем бин
    }
}
//для справки бин это обьект который спринг создает один раз на все приложение те авто-врайды или иньекции через конструкторы это просьба к спрингу чтобы получить бин
//обычно спринг генерирует все под капотом но это конфигурация из за этого приходится создавать вручную зато гибкость большая
//вроде что то начинаю понимать
//этот метод обучения значительно лучше простого чтение переписывание чужого кода с комментариями большой поклон тебе