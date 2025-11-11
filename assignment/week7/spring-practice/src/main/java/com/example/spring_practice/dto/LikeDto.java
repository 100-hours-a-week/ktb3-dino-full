package com.example.spring_practice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class LikeDto {

    @Getter @Builder
    public static class ToggleResponse {
        private boolean liked;
        private int likesCount;
    }
}
