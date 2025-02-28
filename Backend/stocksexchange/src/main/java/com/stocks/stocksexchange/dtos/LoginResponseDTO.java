package com.stocks.stocksexchange.dtos;

import com.stocks.stocksexchange.helper.MyToken;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDTO {
    private MyToken token;
    private String accountId;
}
