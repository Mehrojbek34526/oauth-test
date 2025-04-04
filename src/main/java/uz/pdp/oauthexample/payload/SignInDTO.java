package uz.pdp.oauthexample.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 Created by: Mehrojbek
 DateTime: 12/02/25 20:40
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SignInDTO {

    private String username;

    private String password;

}
