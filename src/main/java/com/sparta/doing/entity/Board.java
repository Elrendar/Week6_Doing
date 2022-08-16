package com.sparta.doing.entity;

import com.sparta.doing.controller.request.BoardDto;
import lombok.*;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Builder
@Getter
@ToString(callSuper = true)
@Table(indexes = {
        @Index(columnList = "boardTitle"),
        @Index(columnList = "boardHashtag"),
        @Index(columnList = "createdAt")
//        @Index(columnList = "createdBy")
})
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PROTECTED)
@Entity
public class Board extends TimeStamp{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 게시판 Id

    @Column(nullable = false)
    private String boardTitle; // 게시판 제목

    @Column(nullable = false)
    private String authorName; // 게시판 작성자

    @Column(nullable = false)
    private String boardContent; // 게시판 내용(TextBox)
//    private String boardThumbnail; // 게시판 썸네일
    private String boardHashtag; // 게시판 해시태그

//    private Long boardLikeCount; // 게시판 좋아요 갯수
//    private Long countBoardVisit; // 게시판 방문자 수
//    private Long countPost; // 게시판에 딸려있는 게시글 수

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "FK_userentity_board"))
    private UserEntity userEntity;

    public void mapToUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
        userEntity.mapToBoard(this);
    }

    public void update(BoardDto boardDto){
        this.boardTitle = boardDto.getBoardTitle();
        this.boardContent = boardDto.getBoardContent();
        this.boardHashtag = boardDto.getBoardHashtag();
    }
}