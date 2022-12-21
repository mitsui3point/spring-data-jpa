package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
//@Rollback(value = false)
public class TeamJpaRepositoryTest {
    @Autowired
    private TeamJpaRepository teamJpaRepository;

    @Test
    void memberJoinTest() {
        //given
        Team team = Team.builder().name("teamA").build();
        //when
        Team savedMember = teamJpaRepository.save(team);
        Team findMember = teamJpaRepository.find(savedMember.getId());
        //then
        assertThat(team.getId()).isEqualTo(findMember.getId());
        assertThat(team.getName()).isEqualTo(findMember.getName());
        assertThat(team).isEqualTo(findMember);//entity 는 같은 id값을 가진 레코드일 경우, 인스턴스도 같다
    }

    @Test
    void memberDeleteTest() {
        //given
        Team teamA = Team.builder().name("teamA").build();
        Team savedTeam = teamJpaRepository.save(teamA);
        //when
        teamJpaRepository.delete(savedTeam);
        Team findTeam = teamJpaRepository.find(savedTeam.getId());
        //then
        assertThat(findTeam).isNull();
    }

    @Test
    void memberFindAllTest() {
        //given
        Team teamA = Team.builder().name("teamA").build();
        Team teamB = Team.builder().name("teamB").build();
        Team teamC = Team.builder().name("teamC").build();
        Team savedTeamA = teamJpaRepository.save(teamA);
        Team savedTeamB = teamJpaRepository.save(teamB);
        Team savedTeamC = teamJpaRepository.save(teamC);
        Team[] expected = {savedTeamA, savedTeamB, savedTeamC};
        //when
        List<Team> actual = teamJpaRepository.findAll();
        //then
        assertThat(actual).containsExactly(expected);
    }

    @Test
    void memberFindByIdTest() {
        //given
        Team teamA = Team.builder().name("teamA").build();
        Team teamB = Team.builder().name("teamB").build();
        Team teamC = Team.builder().name("teamC").build();
        Team savedTeamA = teamJpaRepository.save(teamA);
        Team savedTeamB = teamJpaRepository.save(teamB);
        Team savedTeamC = teamJpaRepository.save(teamC);
        //when
        Optional<Team> actual = teamJpaRepository.findById(teamA.getId());
        //then
        assertThat(actual).isEqualTo(Optional.ofNullable(teamA));
    }

    @Test
    void memberFindByIdNotFoundTest() {
        //given
        Team teamA = Team.builder().name("teamA").build();
        Team teamB = Team.builder().name("teamB").build();
        Team teamC = Team.builder().name("teamC").build();
        Team savedTeamA = teamJpaRepository.save(teamA);
        Team savedTeamB = teamJpaRepository.save(teamB);
        Team savedTeamC = teamJpaRepository.save(teamC);
        //when
        Optional<Team> actual = teamJpaRepository.findById(100L);
        //then
        assertThat(actual).isEqualTo(Optional.empty());
    }

    @Test
    void memberCountTest() {
        //given
        Team teamA = Team.builder().name("teamA").build();
        Team teamB = Team.builder().name("teamB").build();
        Team teamC = Team.builder().name("teamC").build();
        Team savedTeamA = teamJpaRepository.save(teamA);
        Team savedTeamB = teamJpaRepository.save(teamB);
        Team savedTeamC = teamJpaRepository.save(teamC);
        Long expected = 3L;
        //when
        Long actual = teamJpaRepository.count();
        //then
        assertThat(actual).isEqualTo(expected);
    }
}
