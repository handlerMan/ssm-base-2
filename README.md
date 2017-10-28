
## 需要做如下配置：
### 1、在Spring 的配置文件中将 MyBatis的映射文件交付spring管理 
	  <!-- 配置SqlSessionFactory -->
	  <bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
		  ·······
	    <!-- 映射文件配置 -->
	    <property name="mapperLocations" value="classpath*:/**/*Mapper.xml" />   <!--  托管给spring  -->
	  </bean>
### 2、Service层 
   
#### Service接口：继承IBaseService<T>
      public interface IStudentService extends IBaseService<Student> {

      }
      
#### Service 实现类 继承 BaseService<T>
	@Service
	public class StudentService extends BaseService<Student> implements IStudentService {

		@Autowired
		private StudentMapper mapper;

		@Override
		public IBaseDao<Student> getBaseDao() {
		  return mapper;
		}
		//提供实体类的 class 对象
		@Override
		public Class<Student> getClasss() {
			return Student.class;
		}
	}
### 3、dao层
#### 接口：实现 IBaseDao<T>
     public interface StudentMapper extends IBaseDao<Student> {

      }
              
### 4、Controller：继承  BaseController<T>
     
	@Controller 
	@RequestMapping("/stu")
	public class StuController extends BaseController<Student> {
	@Autowired
	private  IStudentService studentService  ;
	//分页查询
	@RequestMapping("/queryByPage")
	public String queryByPage( Model model , Page page, HttpServletRequest request ) {
		page = studentService.queryByPage( page );
		model.addAttribute("page",page);
		System.out.println(page);
		return "stulist";
      		}
    	}
    
### 5、分页界面参考
	<%@ page language="java" contentType="text/html; charset=UTF-8"
	    pageEncoding="UTF-8"%>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>    
	  <div>
	    <c:if test="${page.page==1 }">
	      <a>首页</a>
	      <a>上一页</a>
	    </c:if>
	    <c:if test="${page.page>1 }">
	      <a href="${page.url }&page=1">首页</a>
	      <a href="${page.url }&page=${page.page-1}">上一页</a>
	    </c:if>

	    <!-- 页码 -->
	    <c:forEach items="${page.indexs }" var="index">		
	      <c:if test="${index == page.page }">
		<b>${index}</b>
	      </c:if>
	      <c:if test="${index != page.page }">
		<a href="${page.url }&page=${index}"><b>${index}</b></a>
	      </c:if>
	    </c:forEach>

	    <c:if test="${page.page<page.max }">
	      <a href="${page.url }&page=${page.page+1}">下一页</a>
	      <a href="${page.url }&page=${page.max}">尾页</a>
	    </c:if>
	    <c:if test="${page.page==page.max }">
	      <a >下一页</a>
	      <a >尾页</a>
	    </c:if>

	    <a>当前第${page.page }页/共${page.max }页</a>		
	  </div>

