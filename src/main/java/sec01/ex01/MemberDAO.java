package sec01.ex01;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet; //DB에서 가져온 데이터를 읽음
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;



public class MemberDAO {

	//필
//	private static final String driver="oracle.jdbc.OracleDriver";
//	private static final String url = "jdbc:oracle:thin:@localhost:1521:orcl";
//	private static final String user ="scott";
//	private static final String pwd ="12341234";
	
	private Connection conn;
	//private Statement stmt; //정적 sql문을 실행하고 생성된 결과를 반환하는데 사용되는 개체입니다.
	private PreparedStatement pstmt;
	//PreparedStatement: 미리 컴파일된 SQL 문을 나타내는 개체입니다. 
	//Statement 인터페이스가 DBMS에 전달하는 SQL문은 단순한 문자열이므로 DBMS는 이 문자열을 DBMS가 이해할 수 있도록 컴파일하고 실행함.
	//PreparedStatement 인터페이스는 컴파일된 SQL문을 DBMS에 전달하여 성능을 향상시킴
	//또한 실행하려는 SQL문에 '?'을 넣을 수 있습니다. Statement보다 SQL문 작성하기가 더 간단함. 
	
	private DataSource dataFactory;
	//연결을 위한 팩토리입니다. DriverManeger 기능의 대안
	//JNDI API를 기반으로 하는 이름 지정 서비스에 등록됩니다. 
	
	
	//생
	public MemberDAO() {
		System.out.println("MemberDAO 객체 생성");
		
		try{
			//1.연결을 하기 위한 컨텍스트(project07)인식을 위한 Context객체
			Context ctx=new InitialContext();
			Context envContext =(Context) ctx.lookup("java:/comp/env");
			dataFactory =(DataSource) envContext.lookup("jdbc/oracle");
			//이 코드는 JNDI를 사용하여 "java:/comp/env"에서 "jdbc/oracle"라는 이름으로 등록된 데이터 소스를 
			//얻어와서 dataFactory 변수에 할당한느 것입니다.
			//이렇게 얻어온 데이터 소스를 사용하여 데이터베이스 연결을 설정하고 관리할 수 있습니다
		}
		catch(Exception e) {
			System.out.println("MemberDAO객체에서 DB 연결 관련 에러");
		}
		
		
	}
	//메 
	
	//3.연결 후 회원 목록을 가져와라는 메소드
	public List<MemberVO> listMembers() {
		
		List<MemberVO> list=new ArrayList<MemberVO>();
		try {
		//connDB();
		
		//연결
		conn=dataFactory.getConnection();
		
		//연결객체(conn)가 sql을 돌려야 함, sql을 돌리기 위해서는 sql 관련 문구를 처리하는 PreparedStatement Interface 사용
	
		//4.SQL 작성
		String query="select * from t_member";
		System.out.println("실행한 sql "+query);
		//ResultSet rs=stmt.executeQuery(query); 
		pstmt=conn.prepareStatement(query);
		ResultSet rs=pstmt.executeQuery();
		
		while(rs.next()) {
			//결과테이블(ResultSet)의 칼럼 인식
			String id = rs.getString("id");
			System.out.println("나온 id: "+id);
			String pwd = rs.getString("pwd");
			String name = rs.getString("name");
			String email = rs.getString("email");
			Date joinDate = rs.getDate("joinDate");
			
			//MemberVO 객체를 만들어서 그 객체에 resultSet의 결과를 셋팅해야한다. 
			MemberVO vo =new MemberVO();
			vo.setId(id);
			vo.setPwd(pwd);
			vo.setName(name);
			vo.setEmail(email);
			vo.setJoinDate(joinDate);
		
			list.add(vo);
		}
		rs.close();
		pstmt.close();
		conn.close();
		
		} catch (Exception e) {
			System.out.println("연결시 에러");
		}
		return list;
	}
	
	public void addMember(MemberVO memberVO) {
		try {
			conn=dataFactory.getConnection();
			
			String id=memberVO.getId();
			String pwd=memberVO.getPwd();
			String name=memberVO.getName();
			String email=memberVO.getEmail();
			
			String query="insert into t_member (id,pwd,name,email) values(?,?,?,?)";
			System.out.println("회원추가 sql문: "+query);
			
			pstmt=conn.prepareStatement(query);
			
			pstmt.setString(1,id);
			pstmt.setString(2,pwd);
			pstmt.setString(3,name);
			pstmt.setString(4,email);
			
			pstmt.executeUpdate();
			
			pstmt.close();
			
			
		} catch (SQLException e) {
			System.out.println("회원추가시 에러");
		}
	}
	
	
	
	
	//DB에 연결
//	 void connDB(){
//		 try {
//			 
//			 //1.드라이버 로딩
//			Class.forName(driver);
//			
//			System.out.println("Oracle 드라이버 로딩 성공");
//			
//			//2.연결을 위한 DriverManager클래스가 필요 :JDBC Driver를 관리하며 DB와 연결해서 Connection 구현 객체를 생성, 연결객체 만듬. 
//			conn = DriverManager.getConnection(url, user, pwd);
//			System.out.println("Connection 생성 성공");
//			
//			//stmt=conn.createStatement();
//			
//		} catch (Exception e) {
//			System.out.println("DB연동 관련 에러");
//			System.out.println("에러원인: "+e.getCause().toString());
//		}
//	 }
	
}
