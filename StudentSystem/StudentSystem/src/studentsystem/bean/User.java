package studentsystem.bean;


// 一个用户类，包括用户名，密码，以及用户是否登陆添加其构造器
public class User {
	private String username;	//用户名
	private String password;	//密码
	private int isLogin = 0;	//用户是否登陆
	
	public int getIsLogin() {
		return isLogin;
	}
	public void setIsLogin(int isLogin) {
		this.isLogin = isLogin;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
}
