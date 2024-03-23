package Moodle.Services;

import Moodle.Dto.UserDto;
import Moodle.Model.Users;
import Moodle.Repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegisterService {
    @Autowired
    private UsersRepository repository;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    public void addNewUser(UserDto user) throws Exception{
        if(repository.existsByMail(user.getMail()))
        {
            throw new Exception("User with given mail already exists.");
        }

        Users newUser =new Users();
        newUser.setName(user.getName());
        newUser.setMail(user.getMail());
        newUser.setSurname(user.getSurname());
        newUser.setPassword(encoder.encode(user.getPassword()));

        repository.save(newUser);
    }
}
