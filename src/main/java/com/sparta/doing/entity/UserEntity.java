package com.sparta.doing.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sparta.doing.controller.requestdto.SignUpDto;
import com.sparta.doing.controller.requestdto.UserRequestDto;
import com.sparta.doing.util.UserFunction;
import com.sun.istack.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "users",
        indexes = {@Index(columnList = "email", unique = true)})
// @JsonPropertyOrder({"id", "username", "password", "email", "nickname",
//         "authority", "createdAt", "modifiedAt"})
@Getter
public class UserEntity extends TimeStamp {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id = null;
    @NotNull
    @Column(unique = true, length = 50)
    String username;
    @NotNull
    @JsonIgnore
    String password;
    @NotNull
    @Column(unique = true, length = 100)
    String email;
    @NotNull
    @Column(unique = true, length = 50)
    String nickname;
    @NotNull
    @Enumerated(EnumType.STRING)
    Authority authority;
    @Column
    String profileImageUrl;
    @Column
    String description;

    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "userEntity",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Board> boardList = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "userEntity",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<BoardLike> boardLikeList = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "userEntity",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<PostEntity> posts = new ArrayList<>();

    protected UserEntity() {
        this.username = null;
        this.password = null;
        this.email = null;
        this.nickname = null;
        this.authority = null;
        this.profileImageUrl = null;
        this.description = null;
    }

    @Builder(builderClassName = "buildDefaultUser",
            builderMethodName = "buildDefaultUser",
            access = AccessLevel.PROTECTED)
    protected UserEntity(String username,
                         String password,
                         String email,
                         String nickname,
                         Authority authority,
                         String profileImageUrl,
                         String description) {
        Assert.hasText(username, UserFunction.getClassName() +
                "username이 비어있습니다.");
        Assert.hasText(password, UserFunction.getClassName() + "password가 비어있습니다.");
        Assert.hasText(email, UserFunction.getClassName() + "email이 비어있습니다.");
        Assert.hasText(nickname, UserFunction.getClassName() + "nickname이 비어있습니다.");
        Assert.hasText(authority.toString(), UserFunction.getClassName() + "authority가 비어있습니다.");

        this.username = username;
        this.password = password;
        this.email = email;
        this.nickname = nickname;
        this.authority = authority;
        this.profileImageUrl = profileImageUrl;
        this.description = description == null ? "" : description;
    }

    public static UserEntity of(SignUpDto signUpDto, PasswordEncoder passwordEncoder) {
        return UserEntity.buildDefaultUser()
                .username(signUpDto.getUsername())
                .password(passwordEncoder.encode(signUpDto.getPassword()))
                .email(signUpDto.getEmail())
                .nickname(signUpDto.getNickname())
                .authority(signUpDto.getAuthority() == null ?
                        Authority.ROLE_USER : signUpDto.getAuthority())
                .build();
    }

    public void mapToBoard(Board board) {
        boardList.add(board);
    }

    public void mapToBoardLike(BoardLike boardLike) {
        boardLikeList.add(boardLike);
    }

    public void mapToPost(PostEntity postEntity) {
        posts.add(postEntity);
    }

    @Override
    public boolean equals(Object object) {
        UserEntity userEntity = new UserEntity();
        if (this == object) return true;
        if (!(userEntity.getClass().isInstance(object))) return false;
        return username != null && username.equals(userEntity.getUsername());
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

    public void updateInfo(UserRequestDto requestDto) {
        this.nickname = requestDto.getNickname();
        this.profileImageUrl = requestDto.getProfileImageUrl();
        this.description = requestDto.getDescription();
    }
}
