package study.datajpa.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.repository.MemberRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.slf4j.LoggerFactory.getLogger;
import static study.datajpa.entity.BaseEntityUtil.isEntityAttribute;

@SpringBootTest
@Transactional
public class BaseEntityTest {
    @PersistenceContext
    private EntityManager em;
    @Autowired
    private MemberRepository memberRepository;

    private Logger log = getLogger(BaseEntityTest.class);
    private Member noResultMember;

    @BeforeEach
    void setUp() {
        noResultMember = Member.builder().username("noResultMember").build();
        memberRepository.save(noResultMember);
    }

    @Test
    void jpaEventBaseEntityTest() throws InterruptedException {
        //given
        Member member = Member.builder().username("username1").build();
        memberRepository.save(member);

        Thread.sleep(100);
        member.changeUsername("username2");

        em.flush();//@PreUpdate
        em.clear();

        //when
        Member findMember = memberRepository.findById(member.getId()).orElseGet(() -> noResultMember);

        //then
        assertThat(findMember.getCreatedDate()).isBeforeOrEqualTo(LocalDateTime.now());
        assertThat(findMember.getLastModifiedDate()).isBeforeOrEqualTo(LocalDateTime.now());

        log.info("findMember.getCreatedDate() = " + findMember.getCreatedDate());
        log.info("findMember.getLastModifiedDate() = " + findMember.getLastModifiedDate());
        log.info("findMember.getCreatedBy() = " + findMember.getCreatedBy());
        log.info("findMember.getLastModifiedBy() = " + findMember.getLastModifiedBy());
        assertThat(findMember.getLastModifiedDate()).isAfterOrEqualTo(findMember.getCreatedDate());
    }

    @Test
    void memberCreateTest() {
        //given

        //when
        boolean isExistsCreatedDate = isEntityAttribute(em, Member.class, "createdDate");
        boolean isExistsUpdatedDate = isEntityAttribute(em, Member.class, "lastModifiedDate");
        //then
        assertThat(isExistsCreatedDate).isTrue();
        assertThat(isExistsUpdatedDate).isTrue();
    }

    @Test
    void teamCreateTest() {
        //given

        //when
        boolean isExistsCreatedDate = isEntityAttribute(em, Team.class, "createdDate");
        boolean isExistsUpdatedDate = isEntityAttribute(em, Team.class, "lastModifiedDate");
        //then
        assertThat(isExistsCreatedDate).isTrue();
        assertThat(isExistsUpdatedDate).isTrue();
    }

}