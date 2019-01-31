package jacob.dan.user.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jacob.dan.base.repository.BaseRepository;
import jacob.dan.base.service.impl.BaseServiceImpl;
import jacob.dan.user.entity.User;
import jacob.dan.user.repository.UserRepository;
import jacob.dan.user.service.UserService;

@Service
public class UserServiceImpl extends BaseServiceImpl<User> implements UserService {

	@Autowired
	private UserRepository repository;

	@Override
	public BaseRepository<User> baseRepository() {
		return repository;
	}
}
