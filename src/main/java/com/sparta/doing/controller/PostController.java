package com.sparta.doing.controller;

import com.sparta.doing.controller.requestdto.PostRequestDto;
import com.sparta.doing.controller.responsedto.BoardResponseDto;
import com.sparta.doing.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/boards/{boardId}/posts")
public class PostController {
    private final BoardService boardService;

    @PostMapping
    public ResponseEntity<BoardResponseDto> createPost(@PathVariable Long boardId,
                                                       @RequestBody PostRequestDto postRequestDto) {
        return ResponseEntity.ok(boardService.createPost(boardId, postRequestDto));
    }
}
