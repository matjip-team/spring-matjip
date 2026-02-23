package com.restaurant.matjip.admin.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminUserListResponse {
    private List<AdminUserDetailDto> content;
    private long totalElements;
    private int totalPages;
    private int size;
    private int number;
}
