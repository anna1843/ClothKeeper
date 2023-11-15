package org.project.dev.config.member;

import org.project.dev.config.semiMember.SemiUserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // 이 클래스가 Spring의 설정 파일임을 의미
@EnableWebSecurity // Spring Security 활성화
public class WebSecurityConfigClass {

    // 관리자
    @Configuration
    @Order(0) // 구성 클래스의 우선 순위, 낮을수록 높음
    public static class AdminConfig{

        @Bean // HttpSecurity : 인증, 인가의 세부적인 기능을 설정할 수 있도록 API를 제공해주는 클래스
        public SecurityFilterChain filterChainApp0(HttpSecurity http) throws Exception {
            http.antMatcher("/admin/**")
                    .authorizeHttpRequests()
                    .antMatchers("/", "/login").permitAll()
                    .antMatchers("/admin/index").hasAnyRole("ADMIN")

                    .and()
                    .formLogin()
                    .loginPage("/admin/login")
                    .usernameParameter("memberEmail")
                    .passwordParameter("memberPassword")
                    .loginProcessingUrl("/admin/login/post")
                    .failureUrl("/login")
                    .defaultSuccessUrl("/admin/index")

                    .and()
                    .logout()
                    .logoutUrl("/admin/logout")
                    .logoutSuccessUrl("/")

                    .and()
                    .csrf().disable()
                    .authenticationProvider(adminAuthenticationProvider());
            return http.build();
        }

        @Bean
        public PasswordEncoder adminPasswordEncoder() {
            return new BCryptPasswordEncoder();
        }


        @Bean
        public UserDetailsServiceImpl adminDetailsService(){
            return new UserDetailsServiceImpl();
        }

        @Bean
        public DaoAuthenticationProvider adminAuthenticationProvider(){
            DaoAuthenticationProvider provider=new DaoAuthenticationProvider();
            provider.setUserDetailsService(adminDetailsService());
            provider.setPasswordEncoder(adminPasswordEncoder());
            return provider;
        }
    }

    // 일반 회원
    @Configuration
    @Order(1)
    public static class UserConfig{

        @Bean
        public SecurityFilterChain filterChainApp1(HttpSecurity http) throws Exception {
            http.authorizeHttpRequests()
                    .antMatchers("/", "/member/join", "/member/login", "/product/list").permitAll()
                    .antMatchers("/member/logout", "/member/detail/**", "/member/update/**", "/member/updateImage/**", "/member/delete/**",
                            "/board/**", "/cart/**", "/member/inquiry**", "/member/confirmEmail/**", "/member/confirmPassword/**", "/inquiry/**").authenticated()
                    .antMatchers("/product/manage", "/product/write").hasAnyRole("SELLER", "ADMIN")
                    .antMatchers("/member/list", "/member/pagingList**").hasAnyRole("ADMIN")

                    .and()
                    .formLogin()
                    .loginPage("/member/login")
                    .usernameParameter("memberEmail")
                    .passwordParameter("memberPassword")
                    .loginProcessingUrl("/member/login/post")
                    .failureUrl("/login")
                    .defaultSuccessUrl("/")

                    .and()
                    .oauth2Login()
                    .loginPage("/login")
                    .userInfoEndpoint()
                    .userService(myAuth2UserService());

            http.logout()
                    .logoutUrl("/member/logout")
                    .logoutSuccessUrl("/")

//                    .and()
//                    .exceptionHandling()
//                    .accessDeniedPage("/403")

                    .and()
                    .csrf().disable()
                    .authenticationProvider(userAuthenticationProvider());
            return http.build();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }

        @Bean
        public OAuth2UserService<OAuth2UserRequest, OAuth2User> myAuth2UserService() {
            return new MyOAuth2UserService();
        }

        @Bean
        public UserDetailsServiceImpl userDetailsService(){
            return new UserDetailsServiceImpl();
        }

        @Bean
        public DaoAuthenticationProvider userAuthenticationProvider(){
            DaoAuthenticationProvider provider=new DaoAuthenticationProvider();
            provider.setUserDetailsService(userDetailsService());
            provider.setPasswordEncoder(passwordEncoder());
            return provider;
        }
    }

    // 간편회원
    @Configuration
    @Order(2)
    public static class SemiUserConfig {

        @Bean
        public SecurityFilterChain filterChainApp2(HttpSecurity http) throws Exception {
            http.antMatcher("/semiMember/**")
                    .authorizeHttpRequests()
                    .antMatchers("/", "/login").permitAll()
                    .antMatchers("/semiMember/detail/**").hasAnyRole("SEMIMEMBER", "ADMIN")
                    .antMatchers("/semiMember/pagingList**").hasAnyRole("ADMIN")

                    .and()
                    .formLogin()
                    .loginPage("/semiMember/login")
                    .usernameParameter("semiMemberEmail")
                    .passwordParameter("semiMemberPhone")
                    .loginProcessingUrl("/semiMember/login/post")
                    .failureUrl("/login")
                    .defaultSuccessUrl("/")

                    .and()
                    .logout()
                    .logoutUrl("/semiMember/logout")
                    .logoutSuccessUrl("/")

                    .and()
                    .csrf().disable()
                    .authenticationProvider(semiUserAuthenticationProvider());
            return http.build();
        }

        @Bean
        public SemiUserDetailsServiceImpl semiUserDetailsService() {
            return new SemiUserDetailsServiceImpl();
        }

        @Bean
        public PasswordEncoder semiPasswordEncoder() {
            return new BCryptPasswordEncoder();
        }

        @Bean
        public DaoAuthenticationProvider semiUserAuthenticationProvider() {
            DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
            provider.setUserDetailsService(semiUserDetailsService());
            provider.setPasswordEncoder(semiPasswordEncoder());
            return provider;
        }
    }
}