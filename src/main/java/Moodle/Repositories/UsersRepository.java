package Moodle.Repositories;

import Moodle.Dto.UserDto;
import Moodle.Model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource
public interface UsersRepository extends JpaRepository<Users,Integer> {
        boolean existsByMail (String mail);
        Optional<Users> findByMail(String mail);
}
