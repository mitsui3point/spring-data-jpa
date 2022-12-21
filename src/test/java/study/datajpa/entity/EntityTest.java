package study.datajpa.entity;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
//@Rollback(false)
public class EntityTest {

    @PersistenceContext
    private EntityManager em;

    private Logger log = LoggerFactory.getLogger(EntityTest.class);

    @Test
    void memberTeamTest() {
        Team teamA = Team.builder().name("teamA").build();
        Team teamB = Team.builder().name("teamB").build();

        em.persist(teamA);
        em.persist(teamB);

        Member member1 = Member.builder().username("member1").age(10).team(teamA).build();
        Member member2 = Member.builder().username("member2").age(20).team(teamA).build();
        Member member3 = Member.builder().username("member3").age(30).team(teamB).build();
        Member member4 = Member.builder().username("member4").age(40).team(teamB).build();

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);

        em.flush();
        em.clear();

        List<Member> members = em.createQuery("select m from Member m", Member.class).getResultList();
        for (Member member : members) {
            log.info("member = " + member);
            log.info("member -> team = " + member.getTeam());
            if (member.getUsername().endsWith("1")
                    || member.getUsername().endsWith("2")) {
                assertThat(member.getTeam()).isEqualTo(teamA);
                continue;
            }
            assertThat(member.getTeam()).isEqualTo(teamB);
        }
    }
}
