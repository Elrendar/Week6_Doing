package com.sparta.doing.controller.responsedto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sparta.doing.controller.dto.BoardDto;
import com.sparta.doing.entity.Board;
import com.sparta.doing.entity.PostEntity;
import lombok.*;

import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardResponseDto {
    private Long id;
    private String boardTitle;
    private String authorName;
    private String boardContent;
    private String boardHashtag;
    private int countBoardVisit;
    private String createdAt;
    private List<PostEntity> posts;

    public static BoardResponseDto from(BoardDto boardDto) {
        if (boardDto == null) {
            throw new IllegalArgumentException("Invalid boardDto");
        }
        BoardResponseDto boardResponseDto = new BoardResponseDto();
        boardResponseDto.setId(boardDto.getId());
        boardResponseDto.setBoardTitle(boardDto.getBoardTitle());
        boardResponseDto.setAuthorName(boardDto.getAuthorName());
        boardResponseDto.setBoardContent(boardDto.getBoardContent());
        boardResponseDto.setBoardHashtag(boardDto.getBoardHashtag());
        boardResponseDto.setCountBoardVisit(boardDto.getCountBoardVisit());
        boardResponseDto.setCreatedAt(boardDto.getCreatedAt());
        boardResponseDto.setPosts(boardDto.getPosts());

        return boardResponseDto;
    }

    public static BoardResponseDto from(Board board) {
        return BoardResponseDto.builder()
                .id(board.getId())
                .boardTitle(board.getBoardTitle())
                .boardContent(board.getBoardContent())
                .boardHashtag(board.getBoardHashtag())
                .countBoardVisit(board.getCountBoardVisit())
                .posts(board.getPosts())
                .build();
    }
}
