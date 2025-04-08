package com.mykola.crm.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDetailsResponse {
    private Long id;
    private String username;
    private String email;
    private int yearsOld;
}