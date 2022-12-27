package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

@SpringBootTest
public class SimpleJpaRepositoryTest {
    Logger log = LoggerFactory.getLogger(SimpleJpaRepositoryTest.class);

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Test
    void simpleJpaSaveTest() {
        log.info("memberRepository = {}" , memberRepository);
        log.info("teamRepository = {}" , teamRepository);
        log.info("memberRepository.getClass() = {}" , memberRepository.getClass());
        log.info("teamRepository.getClass() = {}" , teamRepository.getClass());
        log.info("SimpleJpaRepository = {}" , SimpleJpaRepository.class);
    }
}
