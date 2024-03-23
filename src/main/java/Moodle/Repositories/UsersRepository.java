package Moodle.Repositories;

import Moodle.Dto.UserDto;
import Moodle.Model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
@RepositoryRestResource
public interface UsersRepository extends JpaRepository<Users,Long> {
        boolean existsByMail (String mail);
}
