package smoketest.web.staticcontent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: 调式springboot入口
 * @Author NanKong
 * @Date 2022/10/20 19:03
 */
@RestController
@SpringBootApplication
public class BootStarterWebMain {

	@RequestMapping("/")
	String home() {
		return "Hello World!";
	}
	public static void main(String[] args) {
		SpringApplication.run(BootStarterWebMain.class, args);
	}

}
