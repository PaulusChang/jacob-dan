package jacob.dan.user.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jacob.dan.base.repository.BaseRepository;
import jacob.dan.base.service.impl.BaseServiceImpl;
import jacob.dan.user.entity.UserRole;
import jacob.dan.user.repository.UserRoleRepository;
import jacob.dan.user.service.UserRoleService;

@Service
public class UserRoleServiceImpl extends BaseServiceImpl<UserRole> implements UserRoleService {

	@Autowired
	private UserRoleRepository repository;

	@Override
	public BaseRepository<UserRole> baseRepository() {
		return repository;
	}
}
