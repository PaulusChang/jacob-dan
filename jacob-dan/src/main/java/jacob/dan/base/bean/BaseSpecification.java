package jacob.dan.base.bean;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.CriteriaBuilder.In;

import org.springframework.data.jpa.domain.Specification;

import jacob.dan.base.bean.Constraint.Null;
import jacob.dan.base.bean.util.ReflectUtils;
import jacob.dan.base.bean.util.StringUtils;
import jacob.dan.base.constant.YesNo;

public class BaseSpecification <T extends BaseEntity> implements Specification<T> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8776684520990805286L;
	
	protected T t;
	protected Root<T> root;
	protected CriteriaQuery<?> query;
	protected CriteriaBuilder criteriaBuilder;

	public BaseSpecification(T t) {
		super();
		this.t = t;
	}

	@Override
	public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
		if (null == t.getIsDeleted()) {
			t.setIsDeleted(YesNo.NO);
		}
		this.root = root;
		this.query = query;
		this.criteriaBuilder = criteriaBuilder;
		List<Field> fields = ReflectUtils.listFieldsExceptType(t.getClass(), OrderBean.class);
		List<Predicate> predicates = new LinkedList<>();
		for (Field field : fields) {
			if (!ReflectUtils.existValue(t, field)) {
				continue;
			}
			predicates.add(constraint(field, field.getAnnotation(Constraint.class)));
		}
		return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
	}
	
	@SuppressWarnings("unchecked")
	protected Predicate constraint(Field field, Constraint constraint) {
		if (null == constraint) {
			return constraint(field);
		}
		Object objVal = ReflectUtils.getFieldVal(t, field);
		Field aimField = field;
		if (!StringUtils.isEmpty(constraint.name())) {
			aimField = ReflectUtils.getFieldByName(t.getClass(), constraint.name());
		}
		String aimFieldName = aimField.getName();
		Expression<?> expression = root.get(aimFieldName);
		
		if (Null.IS.equals(objVal)) {
			return criteriaBuilder.isNull(expression);
		}
		if (Null.NOT.equals(objVal)) {
			return criteriaBuilder.isNotNull(expression);
		}
		if (objVal instanceof List || objVal instanceof Object[]) {
			return inPredicate(objVal, aimFieldName);
		}
		switch (constraint.type()) {
		case EQUAL:
			return criteriaBuilder.equal(expression, objVal);
			
		case UNEQUAL:
			return criteriaBuilder.notEqual(expression, objVal);
			
		case LIKE:
			if (objVal instanceof String) {
				return criteriaBuilder.like((Expression<String>) expression, String.valueOf(objVal));
			}
		default: 
			
		}

		if (objVal instanceof Number) {
			return numPredicate((Number) objVal, constraint, aimFieldName);
		}
		
		if (objVal instanceof Date) {
			return datePredicate((Date) objVal, constraint, aimFieldName);
		}
		return null;
	}
	
	protected Predicate constraint(Field field) {
		return criteriaBuilder.equal(root.get(field.getName()), ReflectUtils.getFieldVal(t, field));
	}
	
	protected Predicate numPredicate(Number num, Constraint constraint, String fieldName) {
		switch (constraint.type()) {
		
		case MAX_CLOSE:
			return criteriaBuilder.le(root.get(fieldName).as(Number.class), num);

		case MAX_OPEN:
			return criteriaBuilder.lt(root.get(fieldName).as(Number.class), num);
			
		case MIN_CLOSE:
			return criteriaBuilder.ge(root.get(fieldName).as(Number.class), num);
			
		case MIN_OPEN:
			return criteriaBuilder.gt(root.get(fieldName).as(Number.class), num);
			
		default:
			return null;
		}
		
	}
	
	protected Predicate datePredicate(Date date, Constraint constraint, String fieldName) {
		switch (constraint.type()) {
		
		case MAX_CLOSE:
			return criteriaBuilder.lessThanOrEqualTo(root.get(fieldName).as(Date.class), date);

		case MAX_OPEN:
			return criteriaBuilder.lessThan(root.get(fieldName).as(Date.class), date);
			
		case MIN_CLOSE:
			return criteriaBuilder.greaterThanOrEqualTo(root.get(fieldName).as(Date.class), date);
			
		case MIN_OPEN:
			return criteriaBuilder.greaterThan(root.get(fieldName).as(Date.class), date);
			
		default:
			return null;
		}
	}
	/**
	 * 私有方法，就不过多做异常判断了
	 * @author ChangJian
	 * @date 2019年1月17日
	 * @param obj 要求是 objVal instanceof List || objVal instanceof Object[] 为 true
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected Predicate inPredicate(Object obj, String fieldName) {
		List<Object> subObjs;
		if (obj instanceof List) {
			subObjs = (List<Object>) obj;
		} else {
			subObjs = Arrays.asList(obj);
		}
		if (subObjs.size() == 0) {
			return null;
		}
		In<Object> in = criteriaBuilder.in(root.get(fieldName));
		for (Object object : subObjs) {
			in.value(object);
		}
		return in;
	}
	

}
