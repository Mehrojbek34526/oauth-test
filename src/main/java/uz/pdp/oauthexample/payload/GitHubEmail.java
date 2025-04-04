package uz.pdp.oauthexample.payload;

import lombok.Data;

@Data
public class GitHubEmail {
    private String email;
    private boolean primary;
    private boolean verified;
    private String visibility;
}
