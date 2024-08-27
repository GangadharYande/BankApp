package com.boii.bankapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class  UserRequest {
    private String firstName;
    private String lastName;
    private String middleName;
    private String address;
    private String gender;
    private String state;
    private String email;
    private String password;
    private String phoneNumber;
    private String alternatePhoneNumber;

}
