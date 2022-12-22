package study.datajpa.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import javax.persistence.NonUniqueResultException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest
@Transactional
//@Rollback(value = false)
public class MemberRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private TeamRepository teamRepository;
    private Logger log = LoggerFactory.getLogger(MemberRepositoryTest.class);

    private Member noResultMember;
    private Member memberA;
    private Member memberB;
    private Member memberC;
    private Member savedMemberA;
    private Member savedMemberB;
    private Member savedMemberC;

    @BeforeEach
    void setUp() {
        noResultMember = Member.builder().username("noResultMember").build();
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

    @Test
    void memberFindListByUsernameTest() {
        //given
        List<Member> expected = Arrays.asList(memberA);

        //when
        List<Member> actual = memberRepository.findListByUsername(memberA.getUsername());
        //then
        actual.stream().forEach(member -> log.info("member = " + member));
        assertThat(actual).containsExactlyElementsOf(expected);

        //when
        List<Member> actualEmpty = memberRepository.findListByUsername("");
        //then
        log.info("actualEmpty.size(): {}", actualEmpty.size());
        assertThat(actualEmpty).isEmpty();
        assertThat(actualEmpty).isNotNull();
    }

    @Test
    void memberFindMemberByUsernameTest() {
        //given
        Member expected = memberA;

        //when
        Member actual = memberRepository.findMemberByUsername(memberA.getUsername());
        //then
        log.info("actual = {}", actual);
        assertThat(actual).isEqualTo(expected);

        /**
         * 단건으로 지정한 메서드를 호출하면 스프링 데이터 JPA는 내부에서 JPQL의 Query.getSingleResult() 메서드를 호출한다.
         * 이 메서드를 호출했을 때 조회 결과가 없으면 javax.persistence.NoResultException 예외가 발생하는데,
         *      개발자 입장에서 다루기가 상당히 불편하다.
         * 스프링 데이터 JPA는 단건을 조회할 때 이 예외가 발생하면,
         *      예외를 무시하고 대신에 null 을 반환한다.
         */
        //when
        Member actualNull = memberRepository.findMemberByUsername("");
        //then
        log.info("actualNull = {}", actualNull);
        assertThat(actualNull).isNull();
    }

    @Test
    void memberFindOptionalByUsernameTest() {
        //given
        Optional<Member> expected = Optional.of(memberA);
        Member memberDuplicateB = Member.builder().username(memberB.getUsername()).age(memberB.getAge()).build();
        memberRepository.save(memberDuplicateB);

        //when
        Optional<Member> actual = memberRepository.findOptionalByUsername(memberA.getUsername());
        //then
        log.info("actual = {}", actual);
        log.info("expected = {}", expected);
        assertThat(actual).isEqualTo(expected);

        //when
        Optional<Member> actualOptionalEmpty = memberRepository.findOptionalByUsername("");
        //then
        log.info("actualOptionalEmpty = {}", actualOptionalEmpty);
        log.info("actualOptionalEmpty.orElseGet(() -> noResultMember) = {}", actualOptionalEmpty.orElseGet(() -> noResultMember));
        assertThat(actualOptionalEmpty).isNotNull();
        assertThat(actualOptionalEmpty).isEmpty();
        assertThat(actualOptionalEmpty.orElseGet(() -> noResultMember)).isEqualTo(noResultMember);

        /**
         * jpa exception (NonUniqueResultException.class) =>
         *      spring exception (IncorrectResultSizeDataAccessException.class) translation
         *
         *  ex) memberRepository 의 기술은 jpa 가 될수도 있고, mongoDB 등 다른 기술이 될 수 있다.
         *      service 계층의 client code 들은 구현 기술에 의존하는게 아닌,
         *      spring 이 추상화한 예외에 의존하게 해야,
         *      구현 기술을 바꾸게 되더라도 일관된 spring 추상화 예외를 전달받을 수 있기 때문에,
         *      client code 를 바꿀 필요가 없게 된다.
         *      그러한 이점이 있기 때문에 spring 에서 exception 을 translation 하게 된다.
         */
        assertThatExceptionOfType(IncorrectResultSizeDataAccessException.class).isThrownBy(() -> {
            //when
            Optional<Member> actualDuplicate = memberRepository.findOptionalByUsername(memberB.getUsername());
        }).withCauseExactlyInstanceOf(NonUniqueResultException.class);
    }


}