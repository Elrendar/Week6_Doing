package com.sparta.doing.controller;

import com.sparta.doing.controller.requestdto.BoardRequestDto;
import com.sparta.doing.controller.responsedto.BoardResponseDto;
import com.sparta.doing.entity.constant.SearchType;
import com.sparta.doing.service.BoardService;
import com.sparta.doing.service.PaginationService;
import com.sparta.doing.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/boards")
@RestController
public class BoardController {
    private final BoardService boardService;
    private final PaginationService paginationService;

    // (기본 기능)게시판 전체 조회
    // (검색 기능)게시판 검색 및 조회
    // (페이지네이션 기능)페이지네이션
    @GetMapping
    public Page<BoardResponseDto> getpaginateBoards(@RequestParam(required = false) SearchType searchType,
                                                    @RequestParam(required = false) String searchValue,
                                                    @PageableDefault(size = 8, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<BoardResponseDto> boards = boardService.searchBoards(searchType, searchValue, pageable).map(BoardResponseDto::from);
        List<Integer> barNumbers = paginationService.getPaginationBarNumbers(pageable.getPageNumber(), boards.getTotalPages());

        return boards;
    }

    // 1개 게시판 내용 작성 후 BoardController의 @GetMapping으로 이동한 다음, index.html로 이동한다.
    @PostMapping
    public BoardResponseDto createBoard(@RequestBody BoardRequestDto boardRequestDto) {
        var userId = SecurityUtil.getCurrentUserIdByLong();
        return this.boardService.createBoard(boardRequestDto, userId);
    }

    // 1개 게시판 클릭 시 해당 게시판의 내용물과 게시글까지 전부 볼 수 있는 boards.html로 이동한다.
    // 1개 게시판 클릭 시 해당 게시판의 조회수 +1씩 증가.
    @GetMapping("/{boardId}")
    public BoardResponseDto getOneBoard(@PathVariable(name = "boardId") Long boardId) {
        return this.boardService.getOneBoardWithComments(boardId);
    }

    // 1개 게시판 내용 수정 후 BoardController의 @GetMapping("/{boardId}")로 이동한 다음, boards.html로 이동한다.
    // 1개 게시판 내용 수정 Url로 이동 시 인증되지 않은 유저는 login.html로 이동한다.
    @PutMapping("/{boardId}")
    public String updateBoard(@PathVariable(name = "boardId") Long boardId,
                              @RequestBody BoardRequestDto boardRequestDto) {
        var userId = SecurityUtil.getCurrentUserIdByLong();
        this.boardService.updateBoard(boardId, boardRequestDto, userId);
        return "redirect:/boards/{boardId}";
    }

    // 1개 게시판 삭제 후 BoardController의 @GetMapping으로 이동한 다음, index.html로 이동한다.
    // 1개 게시판 내용 수정 Url로 이동 시 인증되지 않은 유저는 login.html로 이동한다.
    @DeleteMapping("/{boardId}")
    public String deleteBoard(@PathVariable(name = "boardId") Long boardId,
                              @AuthenticationPrincipal UserDetails userDetails) {
        var userId = SecurityUtil.getCurrentUserIdByLong();
        this.boardService.deleteBoard(boardId, userId);
        return "redirect:/boards";
    }

    // 게시판 좋아요 클릭 후 BoardController의 @GetMapping으로 이동한 다음, index.html로 이동한다.
    // 1개 게시판 내용 수정 Url로 이동 시 인증되지 않은 유저는 login.html로 이동한다.
    @PostMapping("/{boardId}/like")
    public String boardLike(@PathVariable(name = "boardId") Long boardId,
                            @AuthenticationPrincipal UserDetails userDetails) {
        String userId = userDetails.getUsername();

        if (userId.isEmpty()) {
            return "login";
        }

        boardService.boardLike(boardId, userId);
        return "redirect:/boards";
    }
}
