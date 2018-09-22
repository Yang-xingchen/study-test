package JDBC_text;
//STEP 1. Import required packages
import java.sql.*;

public class DB_main {
	// JDBC driver name and database URL
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	static final String DB_URL = "jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=UTC";

	//  Database credentials
	static final String USER = "root";
	static final String PASS = "816357492";

	static {
		try {
			//STEP 2: Register JDBC driver
			Class.forName(JDBC_DRIVER);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		Connection conn = null;
		Statement stmt = null;
		try{
			//STEP 3: Open a connection
			System.out.println("Connecting to database...");
			conn = DriverManager.getConnection(DB_URL,USER,PASS);

			//STEP 4: Execute a query
			System.out.println("Creating statement...");
			stmt = conn.createStatement();
			String sql;
			
			//�������ݱ�
			sql = "CREATE TABLE user(username varchar(20),email varchar(30),password varchar(16),create_time date)";
			stmt.executeUpdate(sql);
			
			//ɾ�����ݱ�
//			sql = "DROP user";
//			stmt.executeLargeUpdate(sql);
			
			//�½�����
//			sql = "INSERT INTO user "
//					+ "(username,email,password,create_time) "
//					+ "VALUES "
//					+ "('user','test@qq.com','password',NOW());";
//			stmt.executeUpdate(sql);
			
			//ɾ������
//			sql = "DELETE FROM user "
//					+ "WHERE username='user'";
//			stmt.executeUpdate(sql);
			
			//�޸�����
//			sql = "UPDATE user "
//					+ "SET password='654321' "
//					+ "WHERE username='yxc'";
//			stmt.executeUpdate(sql);
			
			//��ѯ����
//			sql = "SELECT username,email,password,create_time"
//					+ " FROM user";
//			ResultSet rs = stmt.executeQuery(sql);
			
			//STEP 5: Extract data from result set
//			while(rs.next()){
//				//Retrieve by column name
//				user user = new user();
//				user.setUsername(rs.getString("username"));
//				user.setEmail(rs.getString("email"));
//				user.setPassword(rs.getString("password"));
//				user.setCreate_time(rs.getDate("create_time"));
//				//Display values
//				System.out.println(user);
//			}
			
			//STEP 6: Clean-up environment
//			rs.close();
			stmt.close();
			conn.close();
		}catch(SQLException se){
			//Handle errors for JDBC
			se.printStackTrace();
		}catch(Exception e){
			//Handle errors for Class.forName
			e.printStackTrace();
		}finally{
			//finally block used to close resources
			try{
				if(stmt!=null)
					stmt.close();
			}catch(SQLException se2){
			}// nothing we can do
			try{
				if(conn!=null)
					conn.close();
			}catch(SQLException se){
				se.printStackTrace();
			}//end finally try
		}//end try
		System.out.println("There are so thing wrong!");
	}//end main
}//end FirstExample