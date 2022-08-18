package com.sparta.doing.config;

import com.sparta.doing.entity.Authority;
import com.sparta.doing.jwt.JwtAccessDeniedHandler;
import com.sparta.doing.jwt.JwtAuthenticationEntryPoint;
import com.sparta.doing.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig /*implements WebMvcConfigurer*/ {
    private final TokenProvider tokenProvider;

    private final CorsFilter corsFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    // @Override
    // public void addCorsMappings(CorsRegistry registry) {
    //     registry.addMapping("/**")
    //             .allowedOriginPatterns("/**")
    //             .allowedMethods("*")
    //             .allowedHeaders("*")
    //             .exposedHeaders("*")
    //             .allowCredentials(true);
    // }

    // @Bean
    // public CorsConfigurationSource corsConfigurationSource() {
    //     CorsConfiguration configuration = new CorsConfiguration();
    //
    //     // configuration.addAllowedOrigin("http://localhost:3000");
    //     configuration.addAllowedOrigin("*");
    //     configuration.addAllowedHeader("*");
    //     configuration.addAllowedMethod("*");
    //
    //     configuration.addExposedHeader("accessToken");
    //     configuration.addExposedHeader("Set-Cookie");
    //
    //     configuration.setAllowCredentials(true);
    //     UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    //     source.registerCorsConfiguration("/**", configuration);
    //     return source;
    // }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        // 로그인 없이 h2 콘솔, 파비콘 사용 허가
        return (web) -> web.ignoring().antMatchers("/h2-console/**"
                , "/favicon.ico"
                , "/error");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                // token을 사용하는 방식이기 때문에 csrf를 disable
                .csrf().disable()

                // // cors 필터 적용
                .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)

                // 예외처리에 직접 구현한 클래스들을 사용하도록 추가
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)

                // h2-console을 위한 설정 추가
                // .and()
                // .headers()
                // .frameOptions()
                // .sameOrigin()

                // 스프링 시큐리티는 기본적으로 세션을 사용
                // 세션을 사용하지 않기 때문에 STATELESS로 설정
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                // // 로그인 시도 할 때 DB에서 조회해 검증할 값(테스트 필요함)
                // .and()
                // .formLogin()
                // .usernameParameter("loginId")
                // .passwordParameter("password")
                .and()
                .logout()
                .logoutUrl("/users/logout")
                // .logoutSuccessUrl("로그인화면")
                // .invalidateHttpSession(true).deleteCookies("JSESSIONID")
                // 로그인, 회원가입 등 토큰이 없을 때 요청이 들어오는 API는 permitAll
                .and()
                .authorizeRequests()
                //.antMatchers("/login").permitAll()
                //.antMatchers("/h2-console/**").permitAll()
                //.antMatchers("/users/auth/**").hasAnyAuthority()

                .antMatchers("/users/auth/renew").hasAnyAuthority(Authority.ROLE_USER.name())
                .antMatchers("/users/**").permitAll()
                .antMatchers("/").permitAll()

                .antMatchers(HttpMethod.GET, "/boards/**").permitAll()
                // .antMatchers(HttpMethod.GET, "/boards/**/posts/**").permitAll()
                // .antMatchers(HttpMethod.POST, "/boards/**/posts/**").hasAnyAuthority(Authority.ROLE_USER.name())
                .antMatchers(HttpMethod.POST, "/boards/**").hasAnyAuthority(Authority.ROLE_USER.name())
                .antMatchers(HttpMethod.PUT, "/boards/**").hasAnyAuthority(Authority.ROLE_USER.name())
                .antMatchers(HttpMethod.DELETE, "/boards/**").hasAnyAuthority(Authority.ROLE_USER.name())


                // 나머지는 전부 인증 필요
                .anyRequest().authenticated()

                // JwtFilter 를 addFilterBefore 로 등록했던 JwtSecurityConfig 클래스를 적용
                .and()
                .apply(new JwtSecurityConfig(tokenProvider));

        return httpSecurity.build();
    }
}
