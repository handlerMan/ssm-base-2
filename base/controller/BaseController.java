package base.controller;

import java.beans.PropertyEditorSupport;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import base.entity.Page;

@Controller
public class BaseController <T> {
	
	@InitBinder
	public void initBinder( WebDataBinder binder , final HttpServletRequest request ){
		binder.registerCustomEditor(Page.class,new PropertyEditorSupport(){
			@Override
			public Object getValue() {
				Page<T> page = (Page<T>) super.getValue();
				if ( page.getPage() == null ) {
					page.setPage(1);
				}
				page.setSize(5);
				//解析request ，获得请求连接
				String url = request.getRequestURI();
				String params = request.getQueryString();//获取参数
				if ( params!=null && !"".equals(params) ) {
					url += "?" + params;
				}else {
					url += "?1=1";
				}
				int index = -1;
				if ( (index = url.indexOf("&page="))!=-1 ) {
					url = url.substring(0, index);
				}
				page.setUrl(url);
//				System.out.println("base："+page);
//				
				return page;
			}
		});
	}
	
}
