package com.sparta.doing.controller;

import com.sparta.doing.controller.requestdto.LoginDto;
import com.sparta.doing.controller.requestdto.SignUpDto;
import com.sparta.doing.controller.requestdto.TokenRequestDto;
import com.sparta.doing.controller.requestdto.UserRequestDto;
import com.sparta.doing.controller.responsedto.TokenDto;
import com.sparta.doing.controller.responsedto.UserResponseDto;
import com.sparta.doing.service.UserService;
import com.sparta.doing.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    // 회원가입 요청
    @PostMapping("/signup")
    public ResponseEntity<UserResponseDto> signup(@RequestBody @Valid SignUpDto signUpDto) {
        return ResponseEntity.ok(userService.signup(signUpDto));
    }

//    이광훈 팀원의 코드입니다. 참고하시면 좋겠습니다.
//    @PostMapping("/signup")
////    @ResponseStatus(value = HttpStatus.CREATED)
//    public Response<UserAccountRequestDto> signup(@RequestBody @Valid SignupRequestDto signupRequestDto) {
//        UserAccount userAccount = userService.signup(signupRequestDto);
////        return Response.getData("회원가입 되었습니다.", UserAccountRequestDto.builder()
////                .userId(userAccount.getUserId())
////                .build());
//        return Response.getData("회원가입 되었습니다.",
//                UserAccountRequestDto.builder()
//                        .userId(userAccount.getUserId())
//                        .build());
//    }

    // 회원 가입 페이지
    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }

    // 로그인 요청
    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody @Valid LoginDto loginDto) {
        return ResponseEntity.ok(userService.login(loginDto));
    }

//    이광훈 팀원의 코드입니다. 참고하시면 좋겠습니다.
//    @PostMapping("/login")
//    public ResponseEntity<Response<JwtTokenDto>> login(@RequestBody @Valid LoginRequestDto loginRequestDto,
//                                                       HttpServletResponse httpServletResponse) {
//        if(this.userService.login(loginRequestDto)){
//            UsernamePasswordAuthenticationToken authenticationToken =
//                    new UsernamePasswordAuthenticationToken(loginRequestDto.getUserId(), loginRequestDto.getPassword());
//
//            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
//
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//
//            String jwtToken = jwtTokenProvider.createToken(authentication);
//
//            Cookie cookie = new Cookie("cookie", jwtToken);
//            cookie.setHttpOnly(true);
//            cookie.setMaxAge(300);
//            httpServletResponse.addCookie(cookie);
//
//            HttpHeaders httpHeaders = new HttpHeaders();
//
//            Response<JwtTokenDto> response =
//                    Response.getData("로그인 되었습니다.", JwtTokenDto.builder().jwtToken(jwtToken).build());
//
//            return new ResponseEntity<>(response, httpHeaders, HttpStatus.OK);
//        } else {
//            Response<JwtTokenDto> response = Response.getData("로그인 실패했습니다. 아이디 또는 비밀번호를 다시 입력해주세요."
//                    , JwtTokenDto.builder().jwtToken("").build());
//            HttpHeaders httpHeaders = new HttpHeaders();
//            return new ResponseEntity<>(response, httpHeaders, HttpStatus.UNAUTHORIZED);
//        }
//    }

    // 회원 로그인 페이지
    @GetMapping("/loginview")
    public String login() {
        return "login";
    }

    // 로그아웃
    @GetMapping("/logout")
    public ResponseEntity<TokenDto> logout() {
        return ResponseEntity.ok(userService.logout());
    }

    //    이광훈 팀원의 코드입니다. 참고하시면 좋겠습니다.
//    @GetMapping("/logout")
//    public ResponseEntity<Response<String>> logout() {
//        HttpHeaders httpHeaders = new HttpHeaders();
//
//        Response<String> response = Response.getData("로그아웃 되었습니다.", null);
//
//        return new ResponseEntity<>(response, httpHeaders, HttpStatus.OK);
//    }
//}
    // 마이페이지
    @GetMapping("/mypage")
    public UserResponseDto getMyInfo() {
        var userId = SecurityUtil.getCurrentUserIdByLong();
        return userService.getUserInfo(userId);
    }

    @PostMapping("/mypage")
    public UserResponseDto updateMyInfo(UserRequestDto userRequestDto) {
        return userService.updateUserInfo(userRequestDto);
    }

    // 토큰 재발급
    @PostMapping("/auth/renew")
    public ResponseEntity<TokenDto> renewToken(@RequestBody TokenRequestDto tokenRequestDto) {
        return ResponseEntity.ok(userService.renewToken(tokenRequestDto));
    }

    // username 중복 검사
    @PostMapping("/dupcheck/username/{username}")
    public ResponseEntity<Boolean> checkUsername(@PathVariable String username) {
        return ResponseEntity.ok(userService.checkUsername(username));
    }

    // email 중복 검사
    @PostMapping("/dupcheck/email/{email}")
    public ResponseEntity<Boolean> checkEmail(@PathVariable String email) {
        return ResponseEntity.ok(userService.checkEmail(email));
    }

    // nickname 중복 검사
    @PostMapping("/dupcheck/nickname/{nickname}")
    public ResponseEntity<Boolean> checkNickname(@PathVariable String nickname) {
        return ResponseEntity.ok(userService.checkNickname(nickname));
    }
}
