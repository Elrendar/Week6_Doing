package com.sparta.doing.controller.requestdto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sparta.doing.controller.dto.BoardDto;
import com.sparta.doing.controller.dto.UserDto;
import lombok.*;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardRequestDto {
    private Long id;
    private String boardTitle;
    private String authorName;
    private String boardContent;
    //    private String boardThumbnail;
    private String boardHashtag;
    private int countBoardVisit;
    private String createdAt;

    public BoardDto toDto(UserDto userDto) {
        return BoardDto.builder()
                .boardTitle(this.boardTitle)
                .authorName(this.authorName)
                .boardContent(this.boardContent)
                .boardHashtag(this.boardHashtag)
                .userDto(userDto)
                .countBoardVisit(this.countBoardVisit)
                .build();
    }
}
