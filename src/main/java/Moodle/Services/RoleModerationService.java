package Moodle.Services;

import Moodle.Model.Role;
import Moodle.Model.Users;
import Moodle.Repositories.UsersRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class RoleModerationService {
    private final UsersRepository repo;
    RoleModerationService(UsersRepository repo){
        this.repo=repo;
    }
    public Boolean giveAdminRole(String mail) throws Exception{
        Users user = repo.findByMail(mail).orElseThrow(()->new UsernameNotFoundException("User not found."));
        user.setRole(Role.admin);
        repo.save(user);
        return true;
    }
    public Boolean giveTutorRole(String mail) throws Exception{
        Users user = repo.findByMail(mail).orElseThrow(()->new UsernameNotFoundException("User not found."));
        user.setRole(Role.tutor);
        repo.save(user);
        return true;
    }
    public Boolean giveStudentRole(String mail) throws Exception{
        Users user = repo.findByMail(mail).orElseThrow(()->new UsernameNotFoundException("User not found."));
        user.setRole(Role.student);
        repo.save(user);
        return true;
    }
}
