package jacob.dan.user.service.impl;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import jacob.dan.base.bean.BaseSpecification;
import jacob.dan.base.bean.util.ReflectUtils;
import jacob.dan.base.bean.util.StringUtils;
import jacob.dan.base.repository.BaseRepository;
import jacob.dan.base.service.impl.BaseServiceImpl;
import jacob.dan.user.entity.User;
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
	
	@Override
	protected Specification<UserRole> specification(UserRole t) {
		return new UserRoleSpecification(t);
	}

	class UserRoleSpecification extends BaseSpecification<UserRole> {

		/**
		 * 
		 */
		private static final long serialVersionUID = 6004809647077289401L;

		public UserRoleSpecification(UserRole t) {
			super(t);
		}

		@Override
		protected Predicate constraint(Field field) {
			if (User.class.equals(field.getType())) {
				return userConstraint(field);
			}
			return super.constraint(field);
		}
		
		private Predicate userConstraint(Field field) {
			User user = (User) ReflectUtils.getFieldVal(t, field);
			if (null != user.getId()) {
				return super.constraint(field);
			}
			if (StringUtils.isEmpty(user.getName())) {
				return null;
			}
			Subquery<User> subquery = query.subquery(User.class);
			Root<User> subqueryRoot = subquery.from(User.class);
			subquery.select(subqueryRoot);
			List<Predicate> predicates = new LinkedList<>();
			predicates.add(criteriaBuilder.equal(subqueryRoot.get("id"), root.get(field.getName())));
			predicates.add(criteriaBuilder.equal(subqueryRoot.get("name"), user.getName()));
			subquery.where(predicates.toArray(new Predicate[predicates.size()]));
			return criteriaBuilder.exists(subquery);
			
		}

	}
}
