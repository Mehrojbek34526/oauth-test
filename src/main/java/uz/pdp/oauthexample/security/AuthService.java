package uz.pdp.oauthexample.security;

import org.springframework.security.core.userdetails.UserDetailsService;
import uz.pdp.oauthexample.payload.SignInDTO;
import uz.pdp.oauthexample.payload.SignUpDTO;

public interface AuthService extends UserDetailsService {

    Object signIn(SignInDTO signInDTO);

    Object signUp(SignUpDTO signUpDTO);
}
