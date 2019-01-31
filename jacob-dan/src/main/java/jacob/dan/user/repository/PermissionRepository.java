package jacob.dan.user.repository;

import org.springframework.stereotype.Repository;

import jacob.dan.base.repository.BaseRepository;
import jacob.dan.user.entity.Permission;

@Repository
public interface PermissionRepository extends BaseRepository<Permission> {

}
