package jacob.dan.user.entity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import jacob.dan.base.bean.BaseEntity;

@Entity
@Table(name = "dan_user_row")
public class UserRole extends BaseEntity {

	@ManyToOne
	private User user;
	@ManyToOne
	private Role role;
	
	public UserRole() {
		super();
	}
	public UserRole(User user, Role role) {
		super();
		this.user = user;
		this.role = role;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}
	
}
