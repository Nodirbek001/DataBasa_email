package com.example.databasa_email.service;

import com.example.databasa_email.entity.User;
import com.example.databasa_email.entity.enums.RoleName;
import com.example.databasa_email.payload.ApiResponse;
import com.example.databasa_email.payload.LoginDto;
import com.example.databasa_email.payload.RegisterDto;
import com.example.databasa_email.repository.RoleRepository;
import com.example.databasa_email.repository.UserRepository;
import com.example.databasa_email.security.JwtProvider;
import com.google.firebase.FirebaseOptions;
import org.assertj.core.internal.bytebuddy.implementation.bytecode.Throw;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Role;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.util.*;

@Service
public class AuthService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    JavaMailSender javaMailSender;
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    Environment env;

    public ApiResponse register(RegisterDto registerDto) {
        boolean b = userRepository.existsByEmail(registerDto.getEmail());
        if (b) {
            return new ApiResponse("bunday email mavjud", false);
        }
        User user = User.builder()
                .firstname(registerDto.getFirstname())
                .lastname(registerDto.getLastname())
                .email(registerDto.getEmail())
                .password(passwordEncoder.encode(registerDto.getPassword()))
                .roles(Collections.singleton(roleRepository.findByRoleName(RoleName.ROLE_USER)))
                .EmailCode(UUID.randomUUID().toString())
                .build();
        userRepository.save(user);

        boolean b1 = sendEmail(user.getEmail(), user.getEmailCode());
        System.out.println(b1);
        return new ApiResponse("emailcodeni jonating", true);


    }

    public boolean sendEmail(String sendingEmail, String code) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(Objects.requireNonNull(env.getProperty("suyunovnodir22@gmail.com")));
            message.setTo(sendingEmail);
            message.setSubject("Tasdiqlash kodi");
            message.setText("<a href='http://localhost:8081/api/auth/verifyEmail?emailCode=" + code + "&email=" + sendingEmail + "'>Tasdiqlang</a>");

            javaMailSender.send(message);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public ApiResponse verifyEmail(String emailCode, String email) {
        Optional<User> byEmailAndEmailCode = userRepository.findByEmail(email);
        if (byEmailAndEmailCode.isPresent() && byEmailAndEmailCode.get().getEmailCode().equals(emailCode)) {
            User user = byEmailAndEmailCode.get();
            user.setEnabled(true);
            user.setEmailCode(null);
            userRepository.save(user);
            return new ApiResponse("akkkaunt tasdiqlandi", true);
        }
        return new ApiResponse("akkaunt tasdiqlanmadi", false);
    }

    public ApiResponse login(LoginDto loginDto) {
        try {
            Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginDto.getUsername(),
                    loginDto.getPassword()
            ));
            User principal = (User) authenticate.getPrincipal();
            String s = jwtProvider.GenerateToken(loginDto.getUsername(), principal.getRoles());
            return new ApiResponse("Token", true, s);
        } catch (BadCredentialsException ignored) {
            return new ApiResponse("Parol yoki login xato", false);
        }
        
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        Optional<User> byEmail = userRepository.findByEmail(username);
//        if (byEmail.isPresent()){
//            return byEmail.get();
//        }
//        throw  new UsernameNotFoundException(username+"not fpund");
        return userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("user topilmadi"));
    }
}
