package utility;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {

	public static String getDBUrl() {
		try(InputStream input = Config.class.getClassLoader().getResourceAsStream("config.properties")){
			Properties prop = new Properties();
			prop.load(input);
			System.out.println(prop.getProperty("db.user"));
			return "jdbc:mysql://"+ prop.getProperty("db.url")+":"+prop.getProperty("db.port")+"/"+prop.getProperty("db.name")+"?user="+prop.getProperty("db.user")+"&password="+prop.getProperty("db.password")+"&serverTimezone=UTC";
		}catch(IOException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public static int getDefaultRoleId() {
		try(InputStream input = Config.class.getClassLoader().getResourceAsStream("config.properties")){
			Properties prop = new Properties();
			prop.load(input);
			String defaultRole = prop.getProperty("default_role_id");
			System.out.println(defaultRole);
			return Integer.valueOf(defaultRole);
		}catch(IOException e) {
			e.printStackTrace();
		}
		return -1;
	}
}
