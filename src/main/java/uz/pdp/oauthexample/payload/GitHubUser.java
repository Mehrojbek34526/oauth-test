package uz.pdp.oauthexample.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GitHubUser {
    private String login;
    private String name;
    @JsonProperty("avatar_url")
    private String avatarUrl;
    private String email;
}
