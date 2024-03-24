package Moodle.Services;

import Moodle.Repositories.UsersRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImp implements UserDetailsService {
    private final UsersRepository repository;
    public UserDetailsServiceImp(UsersRepository repository){
        this.repository=repository;
    }
    @Override
    public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {//it's horrible but I have no other idea
        return repository.findByMail(mail)
                .orElseThrow(()->new UsernameNotFoundException("User not found."));
    }
}
