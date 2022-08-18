package com.sparta.doing.repository;

import com.querydsl.core.types.dsl.StringExpression;
import com.sparta.doing.entity.Board;
import com.sparta.doing.entity.QBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

public interface BoardRepository extends
        JpaRepository<Board, Long>,
        QuerydslPredicateExecutor<Board>,
        QuerydslBinderCustomizer<QBoard> {
    Page<Board> findByBoardTitleContaining(String boardTitle, Pageable pageable);

    Page<Board> findByBoardContentContaining(String boardContent, Pageable pageable);

    Page<Board> findByUserEntity_UsernameContaining(String username, Pageable pageable);

    Page<Board> findByUserEntity_NicknameContaining(String nickname, Pageable pageable);

    Page<Board> findByBoardHashtagContaining(String boardHashtag, Pageable pageable);

    @Override
    default void customize(QuerydslBindings bindings, QBoard root) {
        bindings.excludeUnlistedProperties(true);
        bindings.including(root.boardTitle, root.boardContent, root.boardHashtag, root.createdAt);
        bindings.bind(root.boardTitle).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.boardContent).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.boardHashtag).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.createdAt).first(StringExpression::eq);
    }

}
