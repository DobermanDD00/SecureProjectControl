package com.example.buysell.models.UserPackage;

import com.example.buysell.models.Security.Security;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.PrivateKey;
import java.security.PublicKey;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    User user = new User();
    PrivateKey privateKey;
    PublicKey publicKey;
    private String info = "";

    public boolean isCorrect() {
        StringBuilder stringBuilder = new StringBuilder();

        if (user == null)
            stringBuilder.append("Ошибка, user == null\n");
        if (publicKey == null)
            stringBuilder.append("Ошибка, publicKey == null\n");
        if (privateKey == null)
            stringBuilder.append("Ошибка, privateKey == null\n");

        if (!Security.isCorrectPairKeys(publicKey, privateKey))
            stringBuilder.append("Ошибка, publicKey не соответствует privateKey");
        if (!Security.isCorrectPairKeys(user.getPubKey(), privateKey))
            stringBuilder.append("Ошибка, user.publicKey не соответствует privateKey");

        info = stringBuilder.toString();
        return info.length() == 0;
    }
    public boolean isNotEmpty(){
        StringBuilder stringBuilder = new StringBuilder();
        if (user == null)
            stringBuilder.append("Ошибка, user == null\n");
        if (publicKey == null)
            stringBuilder.append("Ошибка, publicKey == null\n");
        if (privateKey == null)
            stringBuilder.append("Ошибка, privateKey == null\n");
        info = stringBuilder.toString();
        return info.length() == 0;
    }

}
