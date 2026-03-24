package com.example.ailearningbbs.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TagStats {
    private String tag;
    private Integer count;
}
