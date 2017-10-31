package base.entity;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;

@Controller
public class Page <T>{
	private Integer page;   //当前页
	private Integer size = 5;//每页默认为5
	private Integer max; //最大页数
	private Integer count;   //项目条数
	private String url; //地址
	private List<Integer> indexs;//要显示的页码
	private Integer indexNum ;//可现实页码范围内的数量
	private List<T> list;
	private String where;
	
	public Page() {
		page = 1;
		size = 5;
		indexNum = 5;
		where = " 1=1 ";
	}
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String contextPath) {
		this.url = contextPath;
	}

	/**
	 * 计算显示的导航页码
	 */
	private void count() {
		Integer page = getPage();
		Integer max = getMax();
		if ( page!=null && max!=null ) {
			int begin = Math.max(page-indexNum, 1);
			int end = Math.min(page+indexNum, max);
			indexs = new ArrayList<>();
			for( int i = begin ; i <= end ; i++ ) {
				indexs.add(i);
			}
		}
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
		count();
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public Integer getMax() {
		return max;
	}

	public void setMax(Integer max) {
		this.max = max;
		count();
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public List<Integer> getIndexs() {
		return indexs;
	}

	public void setIndexs(List<Integer> indexs) {
		this.indexs = indexs;
	}

	public Integer getIndexNum() {
		return indexNum;
	}

	public void setIndexNum(Integer indexNum) {
		this.indexNum = indexNum;
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	public String getWhere() {
		return where;
	}

	public void setWhere(String where) {
		this.where = where;
	}

	public Page(Integer page, Integer size, Integer max, Integer count, String contextPath, List<Integer> indexs,
			Integer indexNum, List<T> list, String where) {
		super();
		this.page = page;
		this.size = size;
		this.max = max;
		this.count = count;
		this.url = contextPath;
		this.indexs = indexs;
		this.indexNum = indexNum;
		this.list = list;
		this.where = where;
	}

	@Override
	public String toString() {
		return "Page [page=" + page + ", size=" + size + ", max=" + max + ", count=" + count + ", contextPath="
				+ url + ", indexs=" + indexs + ", indexNum=" + indexNum + ", list=" + list + ", where=" + where
				+ "]";
	}
	
	
	
}
