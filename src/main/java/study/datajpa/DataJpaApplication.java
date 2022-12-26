package study.datajpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;
import java.util.UUID;

//@EnableJpaRepositories(basePackages = "jpabook.jpashop.repository")//스프링 부트 사용시 @SpringBootApplication 위치를 지정(해당 패키지와 하위 패키지 인식)
@EnableJpaAuditing//Auditing; createdDate, modifiedDate, createdBy, modifiedBy..//(modifyOnCreate = false) => update Null 등록
@SpringBootApplication
public class DataJpaApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataJpaApplication.class, args);
	}

	@Bean
	public AuditorAware<String> auditorProvider() {
		//return () -> getSession().getId()..
		return () -> Optional.of(UUID.randomUUID().toString());
	}

}
