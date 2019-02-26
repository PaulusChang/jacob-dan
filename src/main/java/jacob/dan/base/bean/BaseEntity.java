package jacob.dan.base.bean;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import com.alibaba.fastjson.annotation.JSONField;

import jacob.dan.base.bean.Constraint.Type;
import jacob.dan.base.bean.util.StringUtils;
import jacob.dan.base.factory.SnowflakeIdFactory;

/**
 * @author ChangJian
 * @date 2019-01-23
 */
@MappedSuperclass
public class BaseEntity {

	@Id
	@Column(length = 20)
	protected String id;
	@JSONField(serialize = false)
	protected Date gmtCreate;
	@Transient
	@Constraint(name = "gmtCreate", type = Type.EQUAL)
	protected Date gmtGreateMax;
	@JSONField(name="gmt_Modify", format="yyyy-MM-dd", ordinal = 3)
	protected Date gmtModify;
	@Transient
	protected OrderBean orderBean;
	protected Short isDeleted;
	
	public BaseEntity() {
	}

	public BaseEntity(boolean init) {
		if (init) {
			init();
		}
	}

	public void init() {
		if (StringUtils.isEmpty(id)) {
			id = SnowflakeIdFactory.getInstance().nextStringId();
		}
		gmtCreate = new Date();
		gmtModify = new Date();
		isDeleted = 0;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Date getGmtCreate() {
		return gmtCreate;
	}
	public void setGmtCreate(Date gmtCreate) {
		this.gmtCreate = gmtCreate;
	}
	public Date getGmtGreateMax() {
		return gmtGreateMax;
	}

	public void setGmtGreateMax(Date gmtGreateMax) {
		this.gmtGreateMax = gmtGreateMax;
	}

	public Date getGmtModify() {
		return gmtModify;
	}
	public void setGmtModify(Date gmtModify) {
		this.gmtModify = gmtModify;
	}

	public OrderBean getOrderBean() {
		return orderBean;
	}

	public void setOrderBean(OrderBean orderBean) {
		this.orderBean = orderBean;
	}

	public Short getIsDeleted() {
		return isDeleted;
	}
	public void setIsDeleted(Short isDeleted) {
		this.isDeleted = isDeleted;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BaseEntity other = (BaseEntity) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return StringUtils.beanString(this);
	}
	
}
