package jacob.dan.base.service.impl;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
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

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import jacob.dan.base.bean.BaseEntity;
import jacob.dan.base.bean.Constraint;
import jacob.dan.base.bean.Constraint.Null;
import jacob.dan.base.bean.OrderBean;
import jacob.dan.base.bean.PageRequestBean;
import jacob.dan.base.bean.util.ReflectUtils;
import jacob.dan.base.bean.util.StringUtils;
import jacob.dan.base.constant.YesNo;
import jacob.dan.base.repository.BaseRepository;
import jacob.dan.base.service.BaseService;

@Service
public abstract class BaseServiceImpl<T extends BaseEntity> implements BaseService<T> {

	public abstract BaseRepository<T> baseRepository();

	protected Specification<T> specification(T t) {
		return new BaseSpecification(t);
	}
	
	protected Sort getSort(T t) {
		List<Field> fields = ReflectUtils.listFieldsByType(t.getClass(), OrderBean.class);
		List<Order> orders = new LinkedList<>();
		for (Field field : fields) {
			OrderBean orderBean = (OrderBean) ReflectUtils.getFieldVal(t, field);
			if (null == orderBean) {
				continue;
			}
			orders.add(orderBean.toOrder());
		}
		if (orders.isEmpty()) {
			return Sort.unsorted();
		}
		return Sort.by(orders);
	}

	@Override
	public void save(T t) {
		t.init();
		baseRepository().save(t);
	}

	@Override
	public T getOne(String id) {
		if (StringUtils.isEmpty(id)) {
			return null;
		}
		return baseRepository().getOne(id);
	}

	@Override
	public List<T> findAll() {
		return findAll(null);
	}

	@Override
	public List<T> findAll(T t) {
		if (null == t) {
			t = newTinstance();
		}
		System.out.println(baseRepository());
		return baseRepository().findAll(specification(t), getSort(t));
	}

	@Override
	public Page<T> findAll(T t, PageRequestBean pageRequestBean) {
		if (null == t) {
			t = newTinstance();
		}
		return baseRepository().findAll(specification(t), pageRequestBean.toPageRequest(getSort(t)));
	}
	
	@SuppressWarnings("unchecked")
	private Class<T> getTclass() {
		return (Class<T>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}
	
	private T newTinstance() {
		return (T) ReflectUtils.newInstance(getTclass());
	}
	
	class BaseSpecification implements Specification<T> {
		
		/**
		 * 
		 */
		public static final long serialVersionUID = -8269011035538040339L;
		
		private T t;
		private Root<T> root;
		private CriteriaBuilder criteriaBuilder;

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
		private Predicate constraint(Field field, Constraint constraint) {
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
		
		private Predicate constraint(Field field) {
			return criteriaBuilder.equal(root.get(field.getName()), ReflectUtils.getFieldVal(t, field));
		}
		
		private Predicate numPredicate(Number num, Constraint constraint, String fieldName) {
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
		
		private Predicate datePredicate(Date date, Constraint constraint, String fieldName) {
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
		private Predicate inPredicate(Object obj, String fieldName) {
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

}
