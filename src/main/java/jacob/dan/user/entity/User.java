package jacob.dan.user.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import jacob.dan.base.bean.BaseEntity;

@Entity
@Table(name = "owg_user_user")
public class User extends BaseEntity {
	
	
	private String openid;
	
	private String accessToken;
	
	private Long accessTokenLimit;
	@Transient
	private Integer expiresIn;
	
	private String refreshToken;
	
	private Long refreshTokenLimit;
	
	private String scope;
	
	private String nickname;
	
	private Short sex;
	
	private String province;
	
	private String city;
	
	private String country;
	
	private String headimgurl;
	
	private String privilege;
	
	private String unionid;
	
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public Long getAccessTokenLimit() {
		return accessTokenLimit;
	}
	public void setAccessTokenLimit(Long accessTokenLimit) {
		this.accessTokenLimit = accessTokenLimit;
	}
	public Integer getExpiresIn() {
		return expiresIn;
	}
	public void setExpiresIn(Integer expiresIn) {
		this.expiresIn = expiresIn;
	}
	public String getRefreshToken() {
		return refreshToken;
	}
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
	public Long getRefreshTokenLimit() {
		return refreshTokenLimit;
	}
	public void setRefreshTokenLimit(Long refreshTokenLimit) {
		this.refreshTokenLimit = refreshTokenLimit;
	}
	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public Short getSex() {
		return sex;
	}
	public void setSex(Short sex) {
		this.sex = sex;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getHeadimgurl() {
		return headimgurl;
	}
	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	}
	public String getPrivilege() {
		return privilege;
	}
	public void setPrivilege(String privilege) {
		this.privilege = privilege;
	}
	public String getUnionid() {
		return unionid;
	}
	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}
	
}
