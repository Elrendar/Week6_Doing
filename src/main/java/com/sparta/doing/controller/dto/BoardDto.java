package com.sparta.doing.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sparta.doing.entity.Board;
import com.sparta.doing.entity.PostEntity;
import com.sparta.doing.entity.UserEntity;
import lombok.*;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardDto {
    private Long id;
    private String boardTitle;
    private String authorName;
    private String boardContent;
    private String boardHashtag;
    private int countBoardVisit;
    private String createdAt;
    private String modifiedAt;
    private UserDto userDto;
    private List<PostEntity> posts;

    // public static BoardDto of(String boardTitle, String authorName, String boardContent, String boardHashtag, int countBoardVisit, UserDto userDto) {
    //     return new BoardDto(null, boardTitle, authorName, boardContent, boardHashtag, countBoardVisit, null, null, userDto);
    // }

    @Builder
    public BoardDto(String boardTitle, String authorName, String boardContent, String boardHashtag, int countBoardVisit, UserDto userDto) {
        this.boardTitle = boardTitle;
        this.authorName = authorName;
        this.boardContent = boardContent;
        this.boardHashtag = boardHashtag;
        this.countBoardVisit = countBoardVisit;
        this.userDto = userDto;
    }

    public static BoardDto from(Board board) {
        BoardDto boardDto = new BoardDto();
        boardDto.setId(board.getId());
        boardDto.setBoardTitle(board.getBoardTitle());
        boardDto.setAuthorName(board.getAuthorName());
        boardDto.setBoardContent(board.getBoardContent());
        boardDto.setBoardHashtag(board.getBoardHashtag());
        boardDto.setCountBoardVisit(board.getCountBoardVisit());
        boardDto.setCreatedAt(board.getCreatedAt());
        boardDto.setModifiedAt(board.getModifiedAt());
        boardDto.setUserDto(UserDto.from(board.getUserEntity()));
        boardDto.setPosts(board.getPosts());

        return boardDto;
    }

    public Board toEntity(UserEntity userEntity) {
        return Board.of(userEntity, boardTitle, boardContent, boardHashtag);
    }
}


