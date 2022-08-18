package com.sparta.doing.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserEntity is a Querydsl query type for UserEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserEntity extends EntityPathBase<UserEntity> {

    private static final long serialVersionUID = -1333903556L;

    public static final QUserEntity userEntity = new QUserEntity("userEntity");

    public final QTimeStamp _super = new QTimeStamp(this);

    public final EnumPath<Authority> authority = createEnum("authority", Authority.class);

    public final ListPath<BoardLike, QBoardLike> boardLikeList = this.<BoardLike, QBoardLike>createList("boardLikeList", BoardLike.class, QBoardLike.class, PathInits.DIRECT2);

    public final ListPath<Board, QBoard> boardList = this.<Board, QBoard>createList("boardList", Board.class, QBoard.class, PathInits.DIRECT2);

    //inherited
    public final StringPath createdAt = _super.createdAt;

    public final StringPath description = createString("description");

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final StringPath modifiedAt = _super.modifiedAt;

    public final StringPath nickname = createString("nickname");

    public final StringPath password = createString("password");

    public final ListPath<PostEntity, QPostEntity> posts = this.<PostEntity, QPostEntity>createList("posts", PostEntity.class, QPostEntity.class, PathInits.DIRECT2);

    public final StringPath username = createString("username");

    public QUserEntity(String variable) {
        super(UserEntity.class, forVariable(variable));
    }

    public QUserEntity(Path<? extends UserEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUserEntity(PathMetadata metadata) {
        super(UserEntity.class, metadata);
    }

}

