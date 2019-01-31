package jacob.dan.user.repository;

import org.springframework.stereotype.Repository;

import jacob.dan.base.repository.BaseRepository;
import jacob.dan.user.entity.User;

@Repository
public interface UserRepository extends BaseRepository<User> {

}
