package uz.pdp.oauthexample.controller;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import uz.pdp.oauthexample.entity.User;
import uz.pdp.oauthexample.payload.*;
import uz.pdp.oauthexample.repository.UserRepository;
import uz.pdp.oauthexample.security.AuthService;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 Created by: Mehrojbek
 DateTime: 12/02/25 20:38
 **/

@Slf4j
@Tag(name = "Authentication api", description = "Authenticationga bog'liq barcha apilar")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final UserRepository userRepository;

    @Value("${app.oauth.google.client-id}")
    private String clientId;

    @Value("${app.oauth.google.client-secret}")
    private String clientSecret;

    @Value("${app.oauth.gitlab.client-id}")
    private String clientIdGitlab;

    @Value("${app.oauth.gitlab.client-secret}")
    private String clientSecretGitlab;

    @Value("${app.oauth.github.client-id}")
    private String clientIdGithub;

    @Value("${app.oauth.github.client-secret}")
    private String clientSecretGithub;


//    private Logger log = Logger.getLogger(AuthController.class.getName());

    @Operation(method = "Method", summary = "This is sign in auth")
    @ApiResponse(description = "This is Api Response")
    @PostMapping("/sign-in")
    public Object signIn(@RequestBody @Parameter(name = "sign in data", required = true) SignInDTO signInDTO) {
        log.info("request data: {}", signInDTO);
        return authService.signIn(signInDTO);
    }

    @PostMapping("/sign-up")
    public Object signUp(@RequestBody SignUpDTO signUpDTO) {
        log.info("request data: {}", signUpDTO);
        return authService.signUp(signUpDTO);
    }

    @GetMapping("/oauth-google-redirect")
    public void googleRedirect(HttpServletResponse response) {

//        String url = "https%3A%2F%2Faccounts.google.com%2Fo%2Foauth2%2Fv2%2Fauth%3Fscope%3Dhttps%3A%2F%2Fwww.googleapis.com%2Fauth%2Fcalendar.readonly%26access_type%3Doffline%26include_granted_scopes%3Dtrue%26response_type%3Dcode%26state%3Dstate_parameter_passthrough_value%26redirect_uri%3Dhttps%3A%2F%2F3456789876.requestcatcher.com%2Ftest%26client_id%3D504070324527-4t60ino9vrpq1auf8h5sikd26hor43c3.apps.googleusercontent.com%0A";
//
        String url = "https://accounts.google.com/o/oauth2/v2/auth?scope=email%20profile&access_type=offline&include_granted_scopes=true&response_type=code&state=state_parameter_passthrough_value&redirect_uri=http%3A%2F%2Flocalhost%3A8080%2Fapi%2Fauth%2Foauth-google-callback&client_id=" + clientId;
        response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
        response.setHeader("Location", url);

//        return ResponseEntity
//                .status(302)
//                .header("Location", url)
//                .build();
    }


    @GetMapping("/oauth-google-callback") // Your redirect URI endpoint
    public String handleGoogleOAuthResponse(
            @RequestParam(name = "code", required = false) String code,
            @RequestParam(name = "state", required = false) String state,
            @RequestParam(name = "error", required = false) String error) {

        if (error != null) {
            return "Error: " + error; // Handle error cases (e.g., user denied access)
        }

        if (code != null) {
            exchangeCodeForToken(code);
            return "Received authorization code: " + code;
            // Exchange the code for an access token (next step)
        }

        return "Invalid response from Google";
    }


    @GetMapping("/oauth-gitlab-link")
    public ResponseEntity<?> handleGitLabLink() {

        String url = "https://gitlab.com/oauth/authorize" +
                "?client_id=" + clientIdGitlab +
                "&redirect_uri=https%3A%2F%2F9523-178-218-201-17.ngrok-free.app%2Fapi%2Fauth%2Foauth-gitlab-callback" +
                "&response_type=code" +
                "&scope=read_user";

        return ResponseEntity.status(302)
                .header("Location", url)
                .build();

    }

    @GetMapping("/oauth-gitlab-callback")
    public void handleGitlabOAuthResponse(@RequestParam String code) {

        String url = "https://gitlab.com/oauth/token"
                + "?client_id=" + clientIdGitlab
                + "&client_secret=" + clientSecretGitlab
                + "&code=" + code
                + "&grant_type=authorization_code"
                + "&redirect_uri=https://9523-178-218-201-17.ngrok-free.app/api/auth/oauth-gitlab-callback";

        RestTemplate restTemplate = new RestTemplate();
        Map response = restTemplate.postForObject(url, null, Map.class);
        System.out.println(response);

        String accessToken = response.get("access_token").toString();

        String userUrl = "https://gitlab.com/api/v4/user";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<?> httpEntity = new HttpEntity<>(headers);

        GitlabUserDTO gitlabUserDTO = restTemplate.exchange(
                userUrl,
                HttpMethod.GET,
                httpEntity,
                GitlabUserDTO.class
        ).getBody();

        Objects.requireNonNull(gitlabUserDTO);

        Optional<User> optionalUser = userRepository.findByUsername(gitlabUserDTO.getEmail());
        if (optionalUser.isPresent()) {
            return;
        }

        User user = new User(
                gitlabUserDTO.getEmail(),
                null,
                true,
                gitlabUserDTO.getName()
        );
        userRepository.save(user);
    }

    @GetMapping("/oauth-github-link")
    public ResponseEntity<?> handleGithubLink() {
        String url = "https://github.com/login/oauth/authorize?client_id=" + clientIdGithub +
                "&scope=user%20user:email";
        return ResponseEntity.status(302)
                .header("Location", url)
                .build();
    }

    @GetMapping("/oauth-github-callback")
    public void handleGithubOAuthResponse(@RequestParam String code) {
        String tokenUrl = "https://github.com/login/oauth/access_token"
                + "?client_id=" + clientIdGithub
                + "&client_secret=" + clientSecretGithub
                + "&code=" + code;
        RestTemplate restTemplate = new RestTemplate();
        Map gitHubToken = restTemplate.postForObject(tokenUrl, null, Map.class);
        String accessToken = gitHubToken.get("access_token").toString();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<?> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<GitHubUser> exchange = restTemplate.exchange(
                "https://api.github.com/user",
                HttpMethod.GET,
                httpEntity,
                GitHubUser.class
        );

        GitHubUser gitHubUser = Objects.requireNonNull(exchange.getBody());

        System.out.println(gitHubUser);

        JavaType type = TypeFactory.defaultInstance().constructParametricType(List.class, GitHubEmail.class);
        ParameterizedTypeReference<List<GitHubEmail>> parameterizedTypeReference = ParameterizedTypeReference.forType(type);

        List<GitHubEmail> gitHubEmails = restTemplate.exchange(
                "https://api.github.com/user/emails",
                HttpMethod.GET,
                httpEntity,
                parameterizedTypeReference
        ).getBody();

        System.out.println(gitHubEmails);

        GitHubEmail gitHubEmail = gitHubEmails.stream()
                .filter(gitHubEmailOne -> gitHubEmailOne.isPrimary())
                .findFirst().get();

        Optional<User> optionalUser = userRepository.findByUsername(gitHubUser.getEmail());
        if (optionalUser.isPresent())
            return;

        User user = new User(
                gitHubEmail.getEmail(),
                null,
                true,
                gitHubUser.getName()
        );
        userRepository.save(user);
    }

    public String exchangeCodeForToken(String code) {
        String tokenUrl = "https://oauth2.googleapis.com/token" +
                "?client_id=" + clientId +
                "&client_secret=" + clientSecret +
                "&grant_type=authorization_code&redirect_uri=http%3A%2F%2Flocalhost%3A8080%2Fapi%2Fauth%2Foauth-google-callback" +
                "&code=" + code;

        RestTemplate restTemplate = new RestTemplate();

        Map response = restTemplate.postForObject(tokenUrl, null, Map.class);

        System.out.println(response);

        return (String) response.get("access_token");
    }

}
