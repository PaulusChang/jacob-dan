package jacob.dan.user.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jacob.dan.base.repository.BaseRepository;
import jacob.dan.base.service.impl.BaseServiceImpl;
import jacob.dan.user.entity.Permission;
import jacob.dan.user.repository.PermissionRepository;
import jacob.dan.user.service.PermissionService;

@Service
public class PermissionServiceImpl extends BaseServiceImpl<Permission> implements PermissionService {

	@Autowired
	private PermissionRepository repository;

	@Override
	public BaseRepository<Permission> baseRepository() {
		return repository;
	}
}
