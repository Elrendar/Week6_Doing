package com.sparta.doing.entity;

import lombok.*;

import javax.persistence.*;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@Entity
public class BoardLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "FK_userentity_boardlike"))
    private UserEntity userEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", foreignKey = @ForeignKey(name = "FK_board_boardlike"))
    private Board board;

    public void mapToUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
        userEntity.mapToBoardLike(this);
    }

    public void mapToBoard(Board board) {
        this.board = board;
        board.mapToBoardLike(this);
    }
}
