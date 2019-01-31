package jacob.dan.user.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import jacob.dan.base.bean.BaseEntity;

@Entity
@Table(name = "owg_user_permission")
public class Permission extends BaseEntity {

	private String name;
	private String value;
	private String description;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
}
