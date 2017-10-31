 package base.service;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.aspectj.org.eclipse.jdt.internal.compiler.lookup.ClassScope;
import org.springframework.stereotype.Service;

import base.annotation.ToOne;
import base.dao.IBaseDao;
import base.entity.Page;

@Service
public abstract class BaseService<T> implements IBaseService<T>{
	
	public abstract IBaseDao<T> getBaseDao(); 
	
	public abstract Class<T> getClasss();
	
	public Class<T> clsss;
	{
		clsss = getClasss();
	}
	
	
	@Override
	public T queryOne(int id) {
		Map<Object, Object> map =  getBaseDao().queryOne(clsss.getSimpleName().toLowerCase(),id).get(0);
		T t = hashMapToEntity(map);
		return t;
	}
	public Map<Object, Object> queryOneByToOne( Class<?> claz, int id) {
		Map<Object, Object> map =  getBaseDao().queryOneByToOne(claz.getSimpleName().toLowerCase(),id).get(0);
		return map;
	}
	

	@Override
	public List<T> queryAll() {
		List<T> ts = new ArrayList<>();
		List<HashMap<Object, Object>> list =  getBaseDao().queryAll( clsss.getSimpleName().toLowerCase() );
		for (HashMap<Object, Object> hashMap : list) {
			ts.add( hashMapToEntity( hashMap ) );
		}
		return ts;
	}

	@Override
	public int add(T t) {
		//获取表名
		String tableName = clsss.getSimpleName().toLowerCase();
		List<Object> list= new ArrayList<>();
		//将参数放入数组中
		for (Field field : t.getClass().getDeclaredFields()) {
			field.setAccessible(true);//权限
			try {
				list.add(field.get(t));
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return getBaseDao().add( tableName , list.toArray() );
	}

	
	@Override
	public void update(T t) {
		int id = 0;
		String tableName = clsss.getSimpleName().toLowerCase();
		List<Object> list= new ArrayList<>();
		for (Field field : t.getClass().getDeclaredFields()) {
			field.setAccessible(true);//权限
			try {
				if ( field.get(t) == null ) {
					continue;
				}
				if (("id").equals( field.getName()) ) { 
					id = (Integer) field.get(t);
					continue ;
				}
				//拼接成 ：变量名='值' 的形式
				list.add( field.getName()+"="+ "'" + field.get(t) + "'" );
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		getBaseDao().update( id, tableName , list.toArray() );
		
	}
	
	
	@Override
	public Page queryByPage(Page page) {
		String tableName = clsss.getSimpleName().toLowerCase();
		List<T> list = new ArrayList<>();
		//查询 数据
		List<HashMap<Object, Object>> listmap =  getBaseDao().queryByPage( tableName, (page.getPage()-1)*page.getSize() , page.getSize() ,page.getWhere() );
		for (HashMap<Object, Object> hashMap : listmap) {
			T t1 = hashMapToEntity(hashMap);
			for (Field field : clsss.getDeclaredFields()) {
				field.setAccessible(true);
				//找到对一的注解
				ToOne toOne = null;
				if ( (toOne =field.getAnnotation(ToOne.class))!=null ) {
					String colum = toOne.column();
					Class<?> cl = toOne.entity();
					Field f = null;
					try {
						f = clsss.getDeclaredField(colum);
						f.setAccessible(true);
						Map<Object, Object> map = queryOneByToOne(cl,  (Integer)f.get(t1) );
						field.set(t1, hashMapToEntity(map, cl ));
					} catch (NoSuchFieldException | SecurityException |IllegalArgumentException | IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
			list.add( t1 );
		}
		//将转换好的数据集合放入 Page 对象
		page.setList(list);
		page.setCount( queryCount( page.getWhere() ) );
		int tmp = page.getCount()/page.getSize();
		page.setMax(page.getCount()<=page.getSize()?1:page.getCount()%page.getSize()>0?tmp+1:tmp);
		return page;
	}
	
	
	//无条件查询记录数
	public int queryCount() {
		return getBaseDao().queryCount( clsss.getSimpleName().toLowerCase() , "1=1" );
	}
	//有条件的查找记录数
	public int queryCount(String where) {
		return getBaseDao().queryCount( clsss.getSimpleName().toLowerCase() , where );
	}
	
	/**
	 * 将HashMap 转成 实体类对象
	 */
	private T hashMapToEntity( Map<Object, Object> map ) {
		T t = null;
		try {
			t = clsss.newInstance();
			for (Field f : t.getClass().getDeclaredFields()) {
				f.setAccessible(true);
				f.set(t,map.get(f.getName()));
			}
		} catch (InstantiationException | IllegalArgumentException | IllegalAccessException e1) {
			e1.printStackTrace();
		}
		return t;
	}
	
	private Object hashMapToEntity( Map<Object, Object> map ,Class<?> cl) {
		Object obj = null;
		try {
			obj = cl.newInstance();
			for (Field f : obj.getClass().getDeclaredFields()) {
				f.setAccessible(true);
				f.set(obj,map.get(f.getName()));
			}
		} catch (InstantiationException | IllegalArgumentException | IllegalAccessException e1) {
			e1.printStackTrace();
		}
		return obj;
	}
	
}
