package Moodle.Services;

import Moodle.Model.Users;
import Moodle.Repositories.UsersRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class ControllerService {
    private final UsersRepository repo;

    public ControllerService(UsersRepository repo) {
        this.repo = repo;
    }

    public Users getAuthenticatedUser(Authentication authentication){
        String mail = authentication.getName(); //it is terrible I know
        return repo.findByMail(mail).get();
    }
}
