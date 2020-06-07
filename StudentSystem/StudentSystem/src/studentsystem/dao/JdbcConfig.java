package studentsystem.dao;

public interface JdbcConfig {
//	新版mysql要加cj连接方式
    String DRIVER = "com.mysql.cj.jdbc.Driver";
//	url格式
    String URL = "jdbc:mysql://localhost:3306/StudentSystemDao";
//	用户名称
    String USERNAME = "root";
//  用户密码
    String PASSWORD = "12345678";
}
