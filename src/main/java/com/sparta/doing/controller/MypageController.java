package com.sparta.doing.controller;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.sparta.doing.controller.request.LoginDto;
import com.sparta.doing.controller.request.SignUpDto;
import com.sparta.doing.controller.request.TokenRequestDto;
import com.sparta.doing.controller.response.TokenDto;
import com.sparta.doing.controller.response.UserResponseDto;
import com.sparta.doing.exception.RefreshTokenNotFoundException;
import com.sparta.doing.service.BoardService;
import com.sparta.doing.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mypage")
public class MypageController {
    private final BoardService boardService;
    private final BoardService customUserDetailsService;
    private final UserService userService;

    @GetMapping("/{id}")
    public UserResponseDto userInfo(@PathVariable Long id) {
        UserResponseDto responseDto = userService.getUserInfo(id);
        return responseDto;
      }
//    @GetMapping("/myposts")
//    public List<PostResponseDto> getUserPost (@AuthenticationPrincipal UserDetailsImpl userDetails) {
//        return postService.getPosts(userDetails.getUser().getUsername());
//    }
//
//    @GetMapping("/mycomments")
//    public List<CommentResponseDto> getUserComments (@AuthenticationPrincipal UserDetailsImpl userDetails){
//        return commentService.getComments(userDetails.getUser().getUsername());
//    }
//
//    @GetMapping("/myreplies")
//    public List<ReplyResponseDto> getUserReplies (@AuthenticationPrincipal UserDetailsImpl userDetails){
//        return replyService.getReplies(userDetails.getUser().getUsername());
//    }
  }