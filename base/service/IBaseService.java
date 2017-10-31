package base.service;

import java.util.List;

import base.entity.Page;

public interface IBaseService <T>{
	 
	T queryOne( int id );
	
	List<T> queryAll();
	
	int add( T t );
	
	void update ( T t );
	
	Page queryByPage( Page<T> page );
	
	
}
