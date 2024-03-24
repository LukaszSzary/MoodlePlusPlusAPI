package Moodle.Services;

import Moodle.Dto.LoginDto;
import Moodle.Dto.UserDto;
import Moodle.Model.Role;
import Moodle.Model.Users;
import Moodle.Repositories.UsersRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {
    private final UsersRepository repository;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    public AuthenticationService(UsersRepository repository,PasswordEncoder passwordEncoder,JwtService jwtService,AuthenticationManager authenticationManager){
        this.repository=repository;
        this.encoder=passwordEncoder;
        this.jwtService=jwtService;
        this.authenticationManager=authenticationManager;
    }


    public void registerUser(UserDto user) throws Exception{
        if(repository.existsByMail(user.getMail()))
        {
            throw new Exception("User with given mail already exists.");
        }

        Users newUser =new Users();
        newUser.setName(user.getName());
        newUser.setMail(user.getMail());
        newUser.setSurname(user.getSurname());
        newUser.setRole(Role.student);
        newUser.setPassword(encoder.encode(user.getPassword()));

        repository.save(newUser);
    }
    public String loginUser(LoginDto request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getMail(),
                        request.getPassword()
                )
        );

        Users user = repository.findByMail(request.getMail()).orElseThrow();
        return jwtService.generateToken(user);

    }
}
