package com.sparta.doing.entity;

import com.sparta.doing.controller.requestdto.BoardRequestDto;
import lombok.*;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

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
public class Board extends TimeStamp {
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

//    private Long countPost; // 게시판에 딸려있는 게시글 수

    @Column(nullable = false)
    private int countBoardVisit; // 게시판 방문자 수

    @Column(nullable = false)
    private int boardLikeCount; // 게시판 좋아요 수

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "FK_userentity_board"))
    private UserEntity userEntity;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BoardLike> boardLikeList = new ArrayList<>();

    public void visit(){
        this.countBoardVisit += 1;
    }

    private Board(UserEntity userEntity, String boardTitle, String boardContent, String boardHashtag) {
        this.userEntity = userEntity;
        this.boardTitle = boardTitle;
        this.boardContent = boardContent;
        this.boardHashtag = boardHashtag;
        this.countBoardVisit = 0;
        this.boardLikeCount = 0;
    }

    public static Board of(UserEntity userEntity, String boardTitle, String boardContent, String boardHashtag) {
        return new Board(userEntity, boardTitle, boardContent, boardHashtag);
    }

    public void mapToUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
        userEntity.mapToBoard(this);
    }

    public void mapToBoardLike(BoardLike boardLike) { boardLikeList.add(boardLike); }

    public void update(BoardRequestDto boardDto) {
        this.boardTitle = boardDto.getBoardTitle();
        this.boardContent = boardDto.getBoardContent();
        this.boardHashtag = boardDto.getBoardHashtag();
    }

    public void updateLikeCount() {
        this.boardLikeCount = this.boardLikeList.size();
    }

    public void discountLike(BoardLike boardLike) {
        this.boardLikeList.remove(boardLike);
    }
}
