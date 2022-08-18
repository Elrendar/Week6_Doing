package com.sparta.doing.controller.responsedto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sparta.doing.entity.Authority;
import com.sparta.doing.entity.UserEntity;
import com.sparta.doing.util.UserFunction;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.extern.jackson.Jacksonized;
import org.springframework.util.Assert;

import javax.validation.constraints.NotBlank;

@Jacksonized
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@FieldDefaults(makeFinal = true, level = AccessLevel.PROTECTED)
public class UserResponseDto {
    @NotBlank
    String username;
    @NotBlank
    String email;
    @NotBlank
    String nickname;
    @JsonIgnore
    @NotBlank
    Authority authority;

    String description;
    String profileImageUrl;

    public static UserResponseDto of(UserEntity userEntity) {
        Assert.notNull(userEntity,
                UserFunction.getClassName() + "userEntity가 비어있습니다.");
        return UserResponseDto.builder()
                .username(userEntity.getUsername())
                .email(userEntity.getEmail())
                .nickname(userEntity.getNickname())
                .authority(userEntity.getAuthority())
                .description(userEntity.getDescription())
                .profileImageUrl(userEntity.getProfileImageUrl())
                .build();
    }
}
