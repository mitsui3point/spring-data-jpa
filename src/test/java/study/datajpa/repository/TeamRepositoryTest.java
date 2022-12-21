package study.datajpa.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Team;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
//@Rollback(value = false)
public class TeamRepositoryTest {
    @Autowired
    private TeamRepository teamRepository;
    private Team noTeam;

    @BeforeEach
    void setUp() {
        noTeam = Team.builder().name("noTeam").build();
    }

    @Test
    void teamJoinTest() {
        //given
        Team team = Team.builder().name("teamA").build();
        //when
        Team savedMember = teamRepository.save(team);
        Team findMember = teamRepository.findById(savedMember.getId()).orElseGet(() -> noTeam);
        //then
        assertThat(team.getId()).isEqualTo(findMember.getId());
        assertThat(team.getName()).isEqualTo(findMember.getName());
        assertThat(team).isEqualTo(findMember);//entity 는 같은 id값을 가진 레코드일 경우, 인스턴스도 같다
    }

    @Test
    void teamDeleteTest() {
        //given
        Team teamA = Team.builder().name("teamA").build();
        Team savedTeam = teamRepository.save(teamA);
        //when
        teamRepository.delete(savedTeam);
        Team findTeam = teamRepository.findById(savedTeam.getId()).orElseGet(() -> noTeam);
        //then
        assertThat(findTeam).isEqualTo(noTeam);
    }

    @Test
    void teamFindAllTest() {
        //given
        Team teamA = Team.builder().name("teamA").build();
        Team teamB = Team.builder().name("teamB").build();
        Team teamC = Team.builder().name("teamC").build();
        Team savedTeamA = teamRepository.save(teamA);
        Team savedTeamB = teamRepository.save(teamB);
        Team savedTeamC = teamRepository.save(teamC);
        Team[] expected = {savedTeamA, savedTeamB, savedTeamC};
        //when
        List<Team> actual = teamRepository.findAll();
        //then
        assertThat(actual).containsExactly(expected);
    }

    @Test
    void teamFindByIdTest() {
        //given
        Team teamA = Team.builder().name("teamA").build();
        Team teamB = Team.builder().name("teamB").build();
        Team teamC = Team.builder().name("teamC").build();
        Team savedTeamA = teamRepository.save(teamA);
        Team savedTeamB = teamRepository.save(teamB);
        Team savedTeamC = teamRepository.save(teamC);
        //when
        Optional<Team> actual = teamRepository.findById(teamA.getId());
        //then
        assertThat(actual).isEqualTo(Optional.ofNullable(teamA));
    }

    @Test
    void teamFindByIdNotFoundTest() {
        //given
        Team teamA = Team.builder().name("teamA").build();
        Team teamB = Team.builder().name("teamB").build();
        Team teamC = Team.builder().name("teamC").build();
        Team savedTeamA = teamRepository.save(teamA);
        Team savedTeamB = teamRepository.save(teamB);
        Team savedTeamC = teamRepository.save(teamC);
        //when
        Optional<Team> actual = teamRepository.findById(100L);
        //then
        assertThat(actual).isEqualTo(Optional.empty());
    }

    @Test
    void teamCountTest() {
        //given
        Team teamA = Team.builder().name("teamA").build();
        Team teamB = Team.builder().name("teamB").build();
        Team teamC = Team.builder().name("teamC").build();
        Team savedTeamA = teamRepository.save(teamA);
        Team savedTeamB = teamRepository.save(teamB);
        Team savedTeamC = teamRepository.save(teamC);
        Long expected = 3L;
        //when
        Long actual = teamRepository.count();
        //then
        assertThat(actual).isEqualTo(expected);
    }
}
