package com.example.buysell.models.Security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.security.PrivateKey;
import java.security.PublicKey;

@AllArgsConstructor
@Getter
public class Keys {
    private final PrivateKey privateKey;
    private final PublicKey publicKey;


}
