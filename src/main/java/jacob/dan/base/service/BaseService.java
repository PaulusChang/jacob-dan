package jacob.dan.base.service;

import java.util.List;

import org.springframework.data.domain.Page;

import jacob.dan.base.bean.BaseEntity;
import jacob.dan.base.bean.PageRequestBean;

public interface BaseService<T extends BaseEntity> {
	
	void save(T t);
	
	T getOne(String id);
	
	List<T> findAll();

	List<T> findAll(T t);
	
	Page<T> findAll(T t, PageRequestBean pageRequestBean);

}
