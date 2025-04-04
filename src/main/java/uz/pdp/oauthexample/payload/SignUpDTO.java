package uz.pdp.oauthexample.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 Created by: Mehrojbek
 DateTime: 12/02/25 21:24
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SignUpDTO {

    private String username;
    private String password;

}
