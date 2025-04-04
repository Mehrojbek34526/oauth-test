package uz.pdp.oauthexample.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 Created by: Mehrojbek
 DateTime: 04/04/25 20:41
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GitlabUserDTO {
    private Long id;
    private String username;
    private String email;
    private String name;
}
