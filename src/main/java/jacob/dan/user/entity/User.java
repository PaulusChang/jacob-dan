package jacob.dan.user.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import jacob.dan.base.bean.Constraint;
import jacob.dan.base.bean.Constraint.Null;
import jacob.dan.base.bean.Constraint.Type;
import jacob.dan.base.bean.NameEntity;

@Entity
@Table(name = "dan_user")
public class User extends NameEntity {
	
	private Integer age;
	
	@Transient
	@Constraint(name = "age")
	private Integer[] ageArray;
	@Transient
	@Constraint(name = "age")
	private List<Integer> ageList;
	@Transient
	@Constraint(name = "age", type = Type.MIN_OPEN)
	private Integer ageMinOpen;
	@Transient
	@Constraint(name = "age", type = Type.MAX_CLOSE)
	private Integer ageMaxClose;
	
	private Float weight;
	
	private Date birthday;
	@Transient
	@Constraint(name = "birthday", type = Type.MIN_OPEN)
	private String birthdayMinOpen;
	@Transient
	@Constraint(name = "birthday", type = Type.MAX_CLOSE)
	private String birthdayMaxClose;

	private String hobby;
	@Transient
	@Constraint(name = "hobby", type = Type.LIKE)
	private String hobbyLike;
	@Transient
	@Constraint(name = "hobby", type = Type.START_WITH)
	private String hobbyStartWith;
	@Transient
	@Constraint(name = "hobby", type = Type.END_WITH)
	private String hobbyEndWith;
	@Transient
	@Constraint(name = "hobby")
	private Null bobbyNull;
	
	public User() {
		super();
	}

	public User(String name, Integer age, Float weight, Date birthday, String hobby) {
		super();
		this.name = name;
		this.age = age;
		this.weight = weight;
		this.birthday = birthday;
		this.hobby = hobby;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Integer[] getAgeArray() {
		return ageArray;
	}

	public void setAgeArray(Integer[] ageArray) {
		this.ageArray = ageArray;
	}

	public List<Integer> getAgeList() {
		return ageList;
	}

	public void setAgeList(List<Integer> ageList) {
		this.ageList = ageList;
	}


	public Integer getAgeMinOpen() {
		return ageMinOpen;
	}

	public void setAgeMinOpen(Integer ageMinOpen) {
		this.ageMinOpen = ageMinOpen;
	}

	public Integer getAgeMaxClose() {
		return ageMaxClose;
	}

	public void setAgeMaxClose(Integer ageMaxClose) {
		this.ageMaxClose = ageMaxClose;
	}

	public Float getWeight() {
		return weight;
	}

	public void setWeight(Float weight) {
		this.weight = weight;
	}

	public String getBirthdayMinOpen() {
		return birthdayMinOpen;
	}

	public void setBirthdayMinOpen(String birthdayMinOpen) {
		this.birthdayMinOpen = birthdayMinOpen;
	}

	public String getBirthdayMaxClose() {
		return birthdayMaxClose;
	}

	public void setBirthdayMaxClose(String birthdayMaxClose) {
		this.birthdayMaxClose = birthdayMaxClose;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getHobby() {
		return hobby;
	}

	public void setHobby(String hobby) {
		this.hobby = hobby;
	}

	public Null getBobbyNull() {
		return bobbyNull;
	}

	public void setBobbyNull(Null bobbyNull) {
		this.bobbyNull = bobbyNull;
	}

	public String getHobbyLike() {
		return hobbyLike;
	}

	public void setHobbyLike(String hobbyLike) {
		this.hobbyLike = hobbyLike;
	}

	public String getHobbyStartWith() {
		return hobbyStartWith;
	}

	public void setHobbyStartWith(String hobbyStartWith) {
		this.hobbyStartWith = hobbyStartWith;
	}

	public String getHobbyEndWith() {
		return hobbyEndWith;
	}

	public void setHobbyEndWith(String hobbyEndWith) {
		this.hobbyEndWith = hobbyEndWith;
	}

	
}
