package com.example.eyagi.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TodayCreatorDto {

    private Long id;
    private String email;
    private String username;
    private String userImage;

}
