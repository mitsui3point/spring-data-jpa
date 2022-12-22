package study.datajpa.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Team;

import java.util.Arrays;
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
    private Team teamA;
    private Team teamB;
    private Team teamC;
    private Team savedTeamA;
    private Team savedTeamB;
    private Team savedTeamC;

    @BeforeEach
    void setUp() {
        noTeam = Team.builder().name("noTeam").build();

        teamA = Team.builder().name("teamA").build();
        teamB = Team.builder().name("teamB").build();
        teamC = Team.builder().name("teamC").build();
        savedTeamA = teamRepository.save(teamA);
        savedTeamB = teamRepository.save(teamB);
        savedTeamC = teamRepository.save(teamC);
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
        List<Team> expected = Arrays.asList(savedTeamA, savedTeamB, savedTeamC);
        
        //when
        List<Team> actual = teamRepository.findAll();
        //then
        assertThat(actual).containsExactlyElementsOf(expected);
    }

    @Test
    void teamFindByIdTest() {
        //given
        //when
        Optional<Team> actual = teamRepository.findById(teamA.getId());
        //then
        assertThat(actual).isEqualTo(Optional.ofNullable(teamA));
    }

    @Test
    void teamFindByIdNotFoundTest() {
        //given
        //when
        Optional<Team> actual = teamRepository.findById(100L);
        //then
        assertThat(actual).isEqualTo(Optional.empty());
    }

    @Test
    void teamCountTest() {
        //given
        Long expected = 3L;
        //when
        Long actual = teamRepository.count();
        //then
        assertThat(actual).isEqualTo(expected);
    }
}
