package com.idm.service.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class Course {
    @NonNull private Long id;
    @NonNull private String username;
    @NonNull private String description;
}
