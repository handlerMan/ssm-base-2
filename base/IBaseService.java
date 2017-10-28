package base;

import java.util.List;

public interface IBaseService <T>{
	 
	T queryOne( int id );
	
	List<T> queryAll();
	
	int add( T t );
	
	void update ( T t );
	
	Page queryByPage( Page<T> page );
	
	
}
