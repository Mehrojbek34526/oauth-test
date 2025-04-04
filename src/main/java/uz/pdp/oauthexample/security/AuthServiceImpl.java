package uz.pdp.oauthexample.security;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.pdp.oauthexample.entity.User;
import uz.pdp.oauthexample.payload.SignInDTO;
import uz.pdp.oauthexample.payload.SignUpDTO;
import uz.pdp.oauthexample.repository.UserRepository;

import java.util.Date;
import java.util.Optional;
import java.util.Random;

/**
 Created by: Mehrojbek
 DateTime: 12/02/25 19:45
 **/
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));

        return user;
    }

    @Override
    public Object signIn(SignInDTO signInDTO) {

        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signInDTO.getUsername(), signInDTO.getPassword())
        );
        User user = (User) authenticate.getPrincipal();
        System.out.println(user.getUsername());

//        User user = userRepository.findByUsername(signInDTO.getUsername())
//                .orElseThrow(() -> new UsernameNotFoundException(signInDTO.getUsername()));
//
//        boolean matches = passwordEncoder.matches(signInDTO.getPassword(), user.getPassword());
//
//        if (!matches)
//            throw new AccessDeniedException("Invalid username or password");

        String token = jwtProvider.generateToken(user.getUsername(), new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000));

        return token;
    }

    @Transactional
    @Override
    public Object signUp(SignUpDTO signUpDTO) {

        Optional<User> optionalUser = userRepository.findByUsername(signUpDTO.getUsername());
        if (optionalUser.isPresent())
            throw new AccessDeniedException("Username already exists");

        User user = new User(
                signUpDTO.getUsername(),
                passwordEncoder.encode(signUpDTO.getPassword()),
                true,
                signUpDTO.getUsername()
        );

        userRepository.save(user);

        Random random = new Random();
        int code = random.nextInt(1000, 9999);

        String to = signUpDTO.getUsername();
        String subject = "Verification code";
        String body = "Verification Code: " + code;

//        mailService.sendMail(to, subject, body);

        return null;
    }

//    @CacheEvict(key = "#username", cacheNames = "userCache")
//    public void deleteKeyFromCache(String username) {
//        //hech qanday ish qilish kerak emas
//    }
//
//    @CachePut(key = "#username", cacheNames = "userCache")
//    public User updateCache(String username) {
//        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
//    }
//
//    public void clearCache(String cacheName) {
//
//        Cache cache = cacheManager.getCache(cacheName);
//        cache.clear();
//    }

    //    public static void main(String[] args) {
//
//        String secretKey = "1873edb12785c17edcd2e633b457bd45b927a6d1aaef01b860e9ab0e5b750dced2fa723ac01bc49bf8e870bdb9cf06c8123ef83ec7680eebbb263d22c836008a7ed8b5c0a224bfbcaba24da036cb786bf30199a2cc87c089380a2449b76b453b961bc68b92c77cfa6863e751a57d91c0c347519d477838563c07267bd9e6cf7af69ea3aa2eee62ed8a44031a14a052f11c5691e4033eaf692bc7f2586a881f32506b0245fc7db6edb85fb6640bdc51815624757d5e132b89fdb09b44656f8e86bd636ea364b5bd8cea990bdc0a650ace3315cd39a10c03c5df7f00edc199283782eb6b1b11e7c67ed92bd5a9f2c089a93203c4eb60403ce6433845cf5490aa0d";
//
//        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes());
//
//        String token = Jwts.builder()
//                .signWith(key)
//                .setSubject("olim12")
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis() - 5 * 60 * 1000))
//                .compact();
//
//        System.out.println(token);
//
//
////        String token2 = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJvbGltMTIiLCJpYXQiOjE3MzkzNzY3NzksImV4cCI6MTczOTM3NjQ3OX0.U9DIB8Zx-qlzzUPsiSRhC1lqJKrKS0rMpGXuWkQspxFdfx5LrjHKZsK9Myv-iKpePV9caAj05v88dePnol0I8A";
////
////        Claims claims = (Claims)Jwts.parserBuilder()
////                .setSigningKey(key)
////                .build()
////                .parse(token2)
////                .getBody();
////
////        String subject = claims.getSubject();
////        System.out.println(subject);
////        Date expiration = claims.getExpiration();
////
////        System.out.println(claims);
//
//    }
}
