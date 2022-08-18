package com.sparta.doing.controller.requestdto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@FieldDefaults(makeFinal = true, level = AccessLevel.PROTECTED)
public class UserRequestDto {
    String nickname;
    String profileImageUrl;
    String description;
}
