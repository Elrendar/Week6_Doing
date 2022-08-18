package com.sparta.doing.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sparta.doing.controller.requestdto.PostRequestDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.util.Assert;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "posts")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
public class PostEntity extends TimeStamp {
    @Id
    @Column(name = "post_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    final Long id = null;
    @NotBlank
    final String content;

    @Setter
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "FK_userentity_post"))
    UserEntity userEntity;

    @Setter
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", foreignKey = @ForeignKey(name = "FK_board_post"))
    Board board;

    protected PostEntity() {
        this.content = null;
        this.board = null;
    }

    @Builder(access = AccessLevel.PROTECTED)
    protected PostEntity(String content, Board board, UserEntity userEntity) {
        Assert.hasText(content, "content가 비어있습니다.");
        Assert.notNull(board, "board가 null입니다.");
        Assert.notNull(userEntity, "userEntity가 null입니다.");

        this.content = content;
        this.board = board;
        this.userEntity = userEntity;
    }

    public static PostEntity of(PostRequestDto postRequestDto, Board board) {
        return PostEntity.builder()
                .content(postRequestDto.getContent())
                .board(board)
                .userEntity(board.getUserEntity())
                .build();
    }

}
