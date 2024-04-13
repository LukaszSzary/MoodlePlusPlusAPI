package Moodle.Services;

import Moodle.Model.Role;
import Moodle.Model.Users;
import Moodle.Repositories.UsersRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModerationService {
    private final UsersRepository repo;
    ModerationService(UsersRepository repo){
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

    public Boolean blockUser(String mail) throws Exception {
        Users user = repo.findByMail(mail).orElseThrow(()->new UsernameNotFoundException("User not found."));
        user.setIsAccountBlocked(true);
        repo.save(user);
        return true;
    }
    public Boolean unBlockUser(String mail) throws Exception {
        Users user = repo.findByMail(mail).orElseThrow(()->new UsernameNotFoundException("User not found."));
        user.setIsAccountBlocked(false);
        repo.save(user);
        return true;
    }

    public List<Users> getAllUsers() {
        return repo.findAll();
    }

    public void deleteUser(int id) throws Exception{
        Users user = repo.findById(id).orElseThrow(()->new UsernameNotFoundException("User not found."));
        repo.delete(user);
    }

    public Users getUser(int id) throws Exception{
        return repo.findById(id).orElseThrow(()->new UsernameNotFoundException("User not found."));
    }
}
