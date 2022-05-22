package com.mino.devjob;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DevjobApplication {
	// embedded MONGO DB에서 m1 맥북 사용 시 에러 발생 방지
	// https://github.com/flapdoodle-oss/de.flapdoodle.embed.mongo/issues/337
	static { System.setProperty("os.arch", "i686_64"); }

	public static void main(String[] args) {
		SpringApplication.run(DevjobApplication.class, args);
	}

}
