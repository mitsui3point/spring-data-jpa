package study.datajpa.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
//@Rollback(value = false)
public class MemberRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private TeamRepository teamRepository;
    private Logger log = LoggerFactory.getLogger(MemberRepositoryTest.class);

    private Member memberA;
    private Member memberB;
    private Member memberC;
    private Member savedMemberA;
    private Member savedMemberB;
    private Member savedMemberC;

    @BeforeEach
    void setUp() {
        memberA = Member.builder().username("usernameA").age(10).build();
        memberB = Member.builder().username("usernameB").age(20).build();
        memberC = Member.builder().username("usernameC").age(30).build();
        savedMemberA = memberRepository.save(memberA);
        savedMemberB = memberRepository.save(memberB);
        savedMemberC = memberRepository.save(memberC);
    }

    @Test
    void memberJoinTest() {
        //given
        Member member = Member.builder().username("usernameA").build();
        //when
        Member savedMember = memberRepository.save(member);
        Member findMember = memberRepository.findById(savedMember.getId()).orElseGet(() -> Member.builder().username("noname!").build());
        //then
        assertThat(member.getId()).isEqualTo(findMember.getId());
        assertThat(member.getUsername()).isEqualTo(findMember.getUsername());
        assertThat(member).isEqualTo(findMember);
    }

    @Test
    void memberRepositoryTest() {
        log.info("memberRepository = " + memberRepository.getClass());
    }

    @Test
    void memberDeleteTest() {
        //given
        Member memberA = Member.builder().username("usernameA").build();
        Member savedMember = memberRepository.save(memberA);
        //when
        memberRepository.delete(savedMember);
        Member noMember = Member.builder().username("nouser").build();
        Member findMember = memberRepository.findById(savedMember.getId()).orElseGet(() -> noMember);
        //then
        assertThat(findMember).isEqualTo(noMember);
    }

    @Test
    void memberFindAllTest() {
        //given
        List<Member> expected = Arrays.asList(savedMemberA, savedMemberB, savedMemberC);
        //when
        List<Member> actual = memberRepository.findAll();
        //then
        assertThat(actual).containsExactlyElementsOf(expected);
    }

    @Test
    void memberFindByIdTest() {
        //given
        //when
        Optional<Member> actual = memberRepository.findById(memberA.getId());
        //then
        assertThat(actual).isEqualTo(Optional.ofNullable(memberA));
    }

    @Test
    void memberFindByIdNotFoundTest() {
        //given
        //when
        Optional<Member> actual = memberRepository.findById(100L);
        //then
        assertThat(actual).isEqualTo(Optional.empty());
    }

    @Test
    void memberCountTest() {
        //given
        Long expected = 3L;
        //when
        Long actual = memberRepository.count();
        //then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void memberFindByUsernameAndAgeGreaterThanTest() {
        //given
        Member memberUnderFifteen = Member.builder().username("AA").age(10).build();
        Member memberOverFifteen = Member.builder().username("AA").age(20).build();
        memberRepository.save(memberUnderFifteen);
        memberRepository.save(memberOverFifteen);
        List<Member> expected = Arrays.asList(memberOverFifteen);
        //when
        /*No property 'username2' found for type 'Member' Did you mean ''username''
        List<Member> actual = memberRepository.findByUsername2AndAgeGreaterThan("AA", 15);*/
        List<Member> actual = memberRepository.findByUsernameAndAgeGreaterThan("AA", 15);
        //then
        assertThat(actual).containsExactlyElementsOf(expected);
    }

    @Test
    void memberFindHelloByTest() {
        //given
        Member[] expected = {memberA, memberB, memberC};
        //when
        List<Member> actual = memberRepository.findHelloBy();
        //then
        assertThat(actual).containsExactly(expected);
    }

    @Test
    void memberFindFirst2Test() {
        //given
        Member[] expected = {memberA, memberB};
        //when
        List<Member> actual = memberRepository.findFirst2By();
        //then
        assertThat(actual).containsExactly(expected);
    }

    @Test
    void memberNamedQueryTest() {
        //given
        Member mem1 = Member.builder().username("AA").age(12).build();
        Member mem2 = Member.builder().username("AA").age(33).build();
        memberRepository.save(mem1);
        memberRepository.save(mem2);
        Member[] expected = {mem1, mem2};
        //when
        List<Member> actual = memberRepository.findByUsername("AA");
        //then
        assertThat(actual).containsExactly(expected);
    }

    @Test
    void memberRepositoryQueryTest() {
        //given
        Member mem1 = Member.builder().username("AA").age(12).build();
        Member mem2 = Member.builder().username("AA").age(33).build();
        memberRepository.save(mem1);
        memberRepository.save(mem2);
        List<Member> expected = Arrays.asList(mem2);
        //when
        List<Member> actual = memberRepository.findUser("AA", 33);
        //then
        assertThat(actual).containsExactlyElementsOf(expected);
    }

    @Test
    void memberFindUsernameListTest() {
        //given
        List<String> expected = Arrays.asList(memberA.getUsername(), memberB.getUsername(), memberC.getUsername());
        //when
        List<String> actual = memberRepository.findUsernameList();
        //then
        actual.stream().forEach(name -> log.info("name = " + name));
        assertThat(actual).containsExactlyElementsOf(expected);
    }

    @Test
    void memberFindMemberDtoTest() {
        //given
        Team teamA = Team.builder().name("teamA").build();
        Team teamB = Team.builder().name("teamB").build();
        teamRepository.save(teamA);
        teamRepository.save(teamB);
        memberA.changeTeam(teamA);
        memberB.changeTeam(teamA);
        memberC.changeTeam(teamB);
        List<MemberDto> expected = Arrays.asList(MemberDto.builder().member(memberA).build(),
                MemberDto.builder().member(memberB).build(),
                MemberDto.builder().member(memberC).build());
        //when
        List<MemberDto> actual = memberRepository.findMemberDto();
        //then
        actual.stream().forEach(memberDto -> log.info("memberDto = " + memberDto));
        assertThat(actual).containsExactlyElementsOf(expected);
    }

    @Test
    void memberFindByNamesTest() {
        //given
        List<Member> expected = Arrays.asList(memberA, memberB);
        //when
        List<Member> actual = memberRepository.findByNames(Arrays.asList(memberA.getUsername(), memberB.getUsername()));
        //then
        actual.stream().forEach(member -> log.info("member = " + member));
        assertThat(actual).containsExactlyElementsOf(expected);
    }

}