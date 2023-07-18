package com.bilgeadam.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Lob;
import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Post {

    private Long id;
    @Lob // big data verileri için
    private String content;
    @Builder.Default
    private LocalDateTime date=LocalDateTime.now();

    private Long userId;
}
