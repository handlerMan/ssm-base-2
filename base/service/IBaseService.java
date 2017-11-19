package base.service;

import java.util.List;

import base.tool.Page;

public interface IBaseService <T>{
	 
	void del( int id );
	
	T queryOne( int id );
	
	List<T> queryAll();
	
	int add( T t );
	
	void update ( T t );
	
	Page<T> queryByPage( Page<T> page );
	
}
