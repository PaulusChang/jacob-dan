package jacob.dan.user.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jacob.dan.base.repository.BaseRepository;
import jacob.dan.base.service.impl.BaseServiceImpl;
import jacob.dan.user.entity.Role;
import jacob.dan.user.repository.RoleRepository;
import jacob.dan.user.service.RoleService;

@Service
public class RoleServiceImpl extends BaseServiceImpl<Role> implements RoleService {

	@Autowired
	private RoleRepository repository;

	@Override
	public BaseRepository<Role> baseRepository() {
		return repository;
	}
}
