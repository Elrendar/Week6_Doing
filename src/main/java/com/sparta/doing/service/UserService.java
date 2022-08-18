package com.sparta.doing.service;

import com.sparta.doing.controller.requestdto.LoginDto;
import com.sparta.doing.controller.requestdto.SignUpDto;
import com.sparta.doing.controller.requestdto.TokenRequestDto;
import com.sparta.doing.controller.requestdto.UserRequestDto;
import com.sparta.doing.controller.responsedto.TokenDto;
import com.sparta.doing.controller.responsedto.UserResponseDto;
import com.sparta.doing.entity.RefreshToken;
import com.sparta.doing.entity.UserEntity;
import com.sparta.doing.exception.DuplicateUserInfoException;
import com.sparta.doing.exception.ExceptionCode;
import com.sparta.doing.exception.InvalidJWTException;
import com.sparta.doing.exception.RefreshTokenNotFoundException;
import com.sparta.doing.jwt.TokenProvider;
import com.sparta.doing.repository.RefreshTokenRepository;
import com.sparta.doing.repository.UserRepository;
import com.sparta.doing.util.SecurityUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public UserResponseDto signup(@Valid SignUpDto signUpDto) {
        if (checkUsername(signUpDto.getUsername())) {
            log.info("이미 가입되어 있는 유저입니다");
            throw new DuplicateUserInfoException("이미 가입되어 있는 유저입니다");
        }

        if (checkEmail(signUpDto.getEmail())) {
            log.info("이미 사용중인 이메일입니다");
            throw new DuplicateUserInfoException("이미 사용중인 이메일입니다");
        }

        if (checkNickname(signUpDto.getNickname())) {
            log.info("이미 사용중인 별명입니다");
            throw new DuplicateUserInfoException("이미 사용중인 별명입니다");
        }

        return UserResponseDto.of(
                userRepository.save(
                        UserEntity.of(signUpDto, passwordEncoder)));
    }

    @Transactional
    public TokenDto login(LoginDto loginDto) {
        // 1. Login 화면에서 입력 받은 ID/PW 를 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());

        // 2. 실제로 검증 (사용자 비밀번호 체크) 이 이루어지는 부분
        //    authenticate 메서드가 실행이 될 때 CustomUserDetailsService 에서 만들었던 loadUserByUsername 메서드가 실행됨
        Authentication authentication;
        try {
            authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        } catch (AuthenticationException e) {
            log.info("아이디, 혹은 비밀번호가 잘못되었습니다.");
            throw new BadCredentialsException("아이디, 혹은 비밀번호가 잘못되었습니다.");
        }

        // 3. 검증이 끝나면 해당 정보로 db에서 UserEntity를 검색
        var userId = Long.valueOf(authentication.getName());
        var userEntity = userRepository.findById(userId)
                .orElseThrow(
                        () -> new UsernameNotFoundException("userId: " + userId +
                                "는 존재하지 않는 회원입니다."));

        // 4. 인증 정보와 PK값을 넣고 JWT 토큰 생성
        TokenDto tokenDto = tokenProvider.createTokenDto(authentication, userId);

        // 5. RefreshToken 저장
        RefreshToken refreshToken = RefreshToken.builder()
                .key(userId)
                .value(tokenDto.getRefreshToken())
                .build();

        refreshTokenRepository.save(refreshToken);

        // 6. 토큰 발급
        return tokenDto;
    }

    // 토큰 재발급
    @Transactional
    public TokenDto renewToken(TokenRequestDto tokenRequestDto) {
        // 1. Refresh Token 검증
        try {
            tokenProvider.validateToken(tokenRequestDto.getRefreshToken());
        } catch (SecurityException | MalformedJwtException e) {
            log.info(ExceptionCode.INVALID_SIGNATURE_TOKEN.getMessage());
            throw new InvalidJWTException(
                    ExceptionCode.INVALID_SIGNATURE_TOKEN.getMessage());
        } catch (ExpiredJwtException e) {
            log.info(ExceptionCode.EXPIRED_TOKEN.getMessage());
            throw new InvalidJWTException(
                    ExceptionCode.EXPIRED_TOKEN.getMessage());
        } catch (UnsupportedJwtException e) {
            log.info(ExceptionCode.UNSUPPORTED_TOKEN.getMessage());
            throw new InvalidJWTException(ExceptionCode.UNSUPPORTED_TOKEN.getMessage());
        } catch (IllegalArgumentException e) {
            log.info(ExceptionCode.WRONG_TOKEN.getMessage());
            throw new InvalidJWTException(ExceptionCode.WRONG_TOKEN.getMessage());
        } catch (Exception e) {
            log.info("{");
            log.info("Exception Message : {}", e.getMessage());
            log.info("Exception StackTrace : {");
            e.printStackTrace();
            log.info("}");
            log.info("================================================");
            throw new InvalidJWTException(ExceptionCode.UNKNOWN_ERROR.getMessage());
        }

        // 2. Access Token 에서 userId(PK) 가져오기
        Authentication authentication = tokenProvider.getAuthentication(tokenRequestDto.getAccessToken());
        var userId = Long.parseLong(authentication.getName());

        // 3. 리프레쉬 토큰 저장소에서 userId(PK) 를 기반으로 토큰 가져옴
        RefreshToken refreshToken = refreshTokenRepository.findById(userId)
                .orElseThrow(() ->
                        new RefreshTokenNotFoundException("로그아웃 된 사용자입니다."));

        // 4. Refresh Token 일치하는지 검사
        if (!refreshToken.getValue().equals(tokenRequestDto.getRefreshToken())) {
            throw new InvalidJWTException("토큰의 유저 정보가 일치하지 않습니다.");
        }

        // 5. Access Token 에서 가져온 userId(PK)를 다시 새로운 토큰의 클레임에 넣고 토큰 생성
        TokenDto tokenDto = tokenProvider.createTokenDto(authentication, userId);

        // 6. db의 리프레쉬 토큰 정보 업데이트
        RefreshToken newRefreshToken =
                refreshToken.withValue(tokenDto.getRefreshToken());
        refreshTokenRepository.save(newRefreshToken);

        // 토큰 발급
        return tokenDto;
    }

    @Transactional
    public TokenDto logout() {
        var userId = SecurityUtil.getCurrentUserIdByLong();
        var token = refreshTokenRepository.findById(userId)
                .orElseThrow(() -> new RefreshTokenNotFoundException(
                        "userId: " + userId + "의 리프레쉬 토큰을 찾을 수 없습니다.")
                );

        refreshTokenRepository.delete(token);

        return tokenProvider.createEmptyTokenDto();
    }

    // 현재 SecurityContext에 있는 유저 정보 가져오기
    @Transactional(readOnly = true)
    public UserResponseDto getMyUserInfoWithAuthorities() {
        var userEntity =
                userRepository.findById(SecurityUtil.getCurrentUserIdByLong())
                        .orElseThrow(
                                () -> new UsernameNotFoundException("로그인 유저 정보가 없습니다."));

        return UserResponseDto.of(userEntity);
    }

    @Transactional(readOnly = true)
    public UserResponseDto getUserInfo(Long userId) {
        return userRepository.findById(userId)
                .map(UserResponseDto::of)
                .orElseThrow(
                        () -> new UsernameNotFoundException(userId + "은 올바른 userId가 아닙니다."));
    }

    @Transactional
    public UserResponseDto updateUserInfo(UserRequestDto requestDto) {
        var userId = SecurityUtil.getCurrentUserIdByLong();
        var user = userRepository.findById(userId)
                .orElseThrow(
                        () -> new UsernameNotFoundException(userId + "은 올바른 userId가 아닙니다."));
        user.updateInfo(requestDto);
        return UserResponseDto.of(userRepository.save(user));
    }

    public boolean checkUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean checkEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean checkNickname(String nickname) {
        return userRepository.existsByNickname(nickname);
    }
}
