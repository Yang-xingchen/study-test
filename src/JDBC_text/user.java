package JDBC_text;

import java.util.Date;

public class user {
	private String username;
	private String email;
	private String password;
	private Date create_time;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Date getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
	@Override
	public String toString() {
		return "user [username=" + username + ", email=" + email + ", password=" + password + ", create_time="
				+ create_time + "]";
	}
	
}
