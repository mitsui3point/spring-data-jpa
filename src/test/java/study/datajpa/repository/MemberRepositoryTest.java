package study.datajpa.repository;

import org.hibernate.proxy.HibernateProxy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.NonUniqueResultException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
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
    @Autowired
    private EntityManager em;
    private Logger log = LoggerFactory.getLogger(MemberRepositoryTest.class);

    private Member noResultMember;
    private Member memberA;
    private Member memberB;
    private Member memberC;
    private Member savedMemberA;
    private Member savedMemberB;
    private Member savedMemberC;

    private Member mem1;
    private Member mem2;
    private Member mem3;
    private Member mem4;
    private Member mem5;

    @BeforeEach
    void setUp() {
        init();
    }

    private void init() {
        noResultMember = Member.builder().username("noResultMember").build();
        memberA = Member.builder().username("usernameA").age(10).build();
        memberB = Member.builder().username("usernameB").age(20).build();
        memberC = Member.builder().username("usernameC").age(30).build();
        savedMemberA = memberRepository.save(memberA);
        savedMemberB = memberRepository.save(memberB);
        savedMemberC = memberRepository.save(memberC);
    }

    private void membersDataSave() {
        mem1 = Member.builder().username("mem1").age(10).build();
        mem2 = Member.builder().username("mem2").age(10).build();
        mem3 = Member.builder().username("mem3").age(10).build();
        mem4 = Member.builder().username("mem4").age(10).build();
        mem5 = Member.builder().username("mem5").age(10).build();
        Arrays.stream(new Member[]{mem1, mem2, mem3, mem4, mem5}).forEach(member -> memberRepository.save(member));
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

    @Test
    void memberFindPageByAgeTest() {
        //given
        int limit = 3;
        int age = 10;
        membersDataSave();
        List<Member> expected = Arrays.asList(memberA, mem5, mem4);
        long expectedTotalCount = Arrays.asList(memberA, mem5, mem4, mem3, mem2, mem1).stream().count();
        //when
        PageRequest pageRequest = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "username"));
        Page<Member> actual = memberRepository.findPageByAge(age, pageRequest);
        Page<MemberDto> memberDtos = actual.map(m -> new MemberDto(m.getId(), m.getUsername(), "tempTeam"));
        //then
        memberDtos.stream().forEach(memberDto -> log.info("memberDto : {}", memberDto));
        assertThat(actual.getContent()).isEqualTo(expected);//조회된 데이터
        assertThat(actual.getContent().size()).isEqualTo(expected.size());//조회된 데이터수
        assertThat(actual.getTotalElements()).isEqualTo(expectedTotalCount);//전체 데이터수
        assertThat(actual.getNumber()).isEqualTo(0);//페이지 번호
        assertThat(actual.getTotalPages()).isEqualTo(2);//전체 페이지 번호
        assertThat(actual.isFirst()).isEqualTo(true);//첫번째 페이지인가?
        assertThat(actual.isLast()).isEqualTo(false);//마지막 페이지인가?
        assertThat(actual.hasPrevious()).isEqualTo(false);//이전페이지가 있는가?
        assertThat(actual.hasNext()).isEqualTo(true);//다음페이지가 있는가?
    }

    @Test
    void memberFindSliceByAgeTest() {
        //given
        int limit = 3;
        int age = 10;
        membersDataSave();
        List<Member> expected = Arrays.asList(memberA, mem5, mem4);
        long expectedTotalCount = Arrays.asList(memberA, mem5, mem4, mem3, mem2, mem1).stream().count();
        //when
        PageRequest pageRequest = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "username"));
        Slice<Member> actual = memberRepository.findSliceByAge(age, pageRequest);
        //then
        assertThat(actual.getContent()).isEqualTo(expected);//조회된 데이터
        assertThat(actual.getContent().size()).isEqualTo(expected.size());//조회된 데이터수
        assertThat(actual.getNumber()).isEqualTo(0);//페이지 번호
        assertThat(actual.isFirst()).isEqualTo(true);//첫번째 페이지인가?
        assertThat(actual.isLast()).isEqualTo(false);//마지막 페이지인가?
        assertThat(actual.hasPrevious()).isEqualTo(false);//이전페이지가 있는가?
        assertThat(actual.hasNext()).isEqualTo(true);//다음페이지가 있는가?
        //assertThat(actual.getTotalElements()).isEqualTo(expectedTotalCount);//전체 데이터수
        //assertThat(actual.getTotalPages()).isEqualTo(2);//전체 페이지 번호
    }

    @Test
    void memberFindMemberAllCountByTest() {
        //given
        int limit = 3;
        int age = 10;
        membersDataSave();
        List<Member> expected = Arrays.asList(memberA, mem5, mem4);
        long expectedTotalCount = Arrays.asList(memberA, mem5, mem4, mem3, mem2, mem1).stream().count();
        //when
        PageRequest pageRequest = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "username"));
        Page<Member> actual = memberRepository.findMemberAllCountBy(age, pageRequest);
        //then
        assertThat(actual.getContent()).isEqualTo(expected);//조회된 데이터
        assertThat(actual.getContent().size()).isEqualTo(expected.size());//조회된 데이터수
        assertThat(actual.getTotalElements()).isEqualTo(expectedTotalCount);//전체 데이터수
        assertThat(actual.getNumber()).isEqualTo(0);//페이지 번호
        assertThat(actual.getTotalPages()).isEqualTo(2);//전체 페이지 번호
        assertThat(actual.isFirst()).isEqualTo(true);//첫번째 페이지인가?
        assertThat(actual.isLast()).isEqualTo(false);//마지막 페이지인가?
        assertThat(actual.hasPrevious()).isEqualTo(false);//이전페이지가 있는가?
        assertThat(actual.hasNext()).isEqualTo(true);//다음페이지가 있는가?
    }

    @Test
    void memberFindListByAgeTest() {
        //given
        int limit = 3;
        int age = 10;
        membersDataSave();
        List<Member> expected = Arrays.asList(memberA, mem5, mem4);
        long expectedTotalCount = Arrays.asList(memberA, mem5, mem4, mem3, mem2, mem1).stream().count();
        //when
        PageRequest pageRequest = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "username"));
        List<Member> actual = memberRepository.findListByAge(age, pageRequest);
        //then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void memberBulkAgePlusTest() {
        //given
        int age = 10;
        //when
        List<Member> findMember = memberRepository.findAll()
                .stream()
                .filter(member -> member.getAge() >= age)
                .collect(toList());
        Map<Long, Integer> expectedMembers = findMember.stream()
                .collect(toMap(
                        Member::getId,
                        Member::getAge));

        int updatedCount = memberRepository.bulkAgePlus(age);//JPQL 실행 후 DBMS 에는 변경사항이 반영됨(em.flush())

        //then
        findMember.stream()
                .map(Member::getId)
                .forEach(memberId -> {
                    Member actualMember = memberRepository.findById(memberId).orElseGet(() -> noResultMember);
                    int actual = actualMember.getAge();
                    int expected = expectedMembers.get(memberId) + 1;
                    assertThat(actual).isEqualTo(expected);
                });
        assertThat(updatedCount).isEqualTo(3);
    }

    @Test
    void memberFindMembersFetchJoinTest() {
        //given
        entityGraphTestDataSet();

        em.flush();
        em.clear();

        //when
        memberRepository.findMembersFetchJoin()
                .stream()
                .forEach(member -> {//select Member 1, member record(N): N + 1
                    Team actual = member.getTeam();

                    //then
                    //FetchType.LAZY, not Apply fetch join: study.datajpa.entity.Team$HibernateProxy$qZsKoHwQ -> proxy instance: 최초 empty instance
                    assertThat(actual).isNotInstanceOf(HibernateProxy.class);
                    assertThat(actual).isInstanceOf(Team.class);
                });
    }

    @Test
    void memberEntityGraphFindAllTest() {
        //given
        entityGraphTestDataSet();

        em.flush();
        em.clear();

        //when: override findAll method
        memberRepository.findAll()
                .stream()
                .forEach(member -> {
                    Team actual = member.getTeam();

                    //then
                    assertThat(actual).isNotInstanceOf(HibernateProxy.class);
                    assertThat(actual).isInstanceOf(Team.class);
                });
    }

    @Test
    void memberFindMembersEntityGraphTest() {
        //given
        entityGraphTestDataSet();

        em.flush();
        em.clear();

        //when
        memberRepository.findMembersEntityGraph()
                .stream()
                .forEach(member -> {
                    Team actual = member.getTeam();

                    //then
                    assertThat(actual).isNotInstanceOf(HibernateProxy.class);
                    assertThat(actual).isInstanceOf(Team.class);
                });
    }

    @Test
    void memberFindMembersEntityGraphByUsernameTest() {
        //given
        entityGraphTestDataSet();

        em.flush();
        em.clear();

        //when
        memberRepository.findMembersEntityGraphByUsername(memberA.getUsername())
                .stream()
                .forEach(member -> {//select Member 1, member record(N): N + 1
                    Team actual = member.getTeam();

                    //then
                    assertThat(actual).isNotInstanceOf(HibernateProxy.class);
                    assertThat(actual).isInstanceOf(Team.class);
                });
    }

    @Test
    void memberFindMembersNamedEntityGraphTest() {
        //given
        entityGraphTestDataSet();

        em.flush();
        em.clear();

        //when
        memberRepository.findMembersNamedEntityGraphByUsername(memberA.getUsername())
                .stream()
                .forEach(member -> {
                    Team actual = member.getTeam();

                    //then
                    assertThat(actual).isNotInstanceOf(HibernateProxy.class);
                    assertThat(actual).isInstanceOf(Team.class);
                });
    }

    private void entityGraphTestDataSet() {
        memberRepository.delete(memberA);
        memberRepository.delete(memberB);
        memberRepository.delete(memberC);

        Team teamA = Team.builder().name("teamA").build();
        Team teamB = Team.builder().name("teamB").build();
        teamRepository.save(teamA);
        teamRepository.save(teamB);

        Member member1 = Member.builder().username("member1").age(10).team(teamA).build();
        Member member2 = Member.builder().username("member2").age(20).team(teamB).build();
        memberRepository.save(member1);
        memberRepository.save(member2);
    }

    @Test
    void queryHintTest() {
        //given
        em.flush();
        em.clear();

        //when
        Member changeMemberA = memberRepository.findReadOnlyByUsername(memberA.getUsername());
        changeMemberA.changeUsername("changeUsername");

        em.flush();//변경감지 동작
        em.clear();

        Member actualChangeUsername = memberRepository.findReadOnlyByUsername("changeUsername");
        Member actualOriginalUsername = memberRepository.findReadOnlyByUsername("usernameA");

        //then
        assertThat(actualChangeUsername).isNull();
        assertThat(actualOriginalUsername).isEqualTo(memberA);
    }

    @Test
    void lockTest() {
        //given
        em.flush();
        em.clear();

        //when
        List<Member> result = memberRepository.findLockByUsername(memberA.getUsername());
        //then
    }

    @Test
    void customTest() {
        //given
        List<Member> members = Arrays.asList(memberA, memberB, memberC);
        //when
        List<Member> actual = memberRepository.findMembersCustom();
        //then
        assertThat(actual).isEqualTo(members);
    }

    private boolean isMemberEntityLazyLoad(Member member) {
        //참고: 다음과 같이 지연 로딩 여부를 확인할 수 있다.
        //Hibernate 기능으로 확인
        //return Hibernate.isInitialized(member.getTeam())
        //JPA 표준 방법으로 확인
        return em.getEntityManagerFactory()
                .getPersistenceUnitUtil()
                .isLoaded(member.getTeam());
    }
}