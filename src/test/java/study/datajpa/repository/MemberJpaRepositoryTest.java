package study.datajpa.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
//@Rollback(value = false)
public class MemberJpaRepositoryTest {
    @Autowired
    private MemberJpaRepository memberJpaRepository;

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
        savedMemberA = memberJpaRepository.save(memberA);
        savedMemberB = memberJpaRepository.save(memberB);
        savedMemberC = memberJpaRepository.save(memberC);
    }

    @Test
    void memberJoinTest() {
        //given
        Member member = Member.builder().username("usernameA").build();
        //when
        Member savedMember = memberJpaRepository.save(member);
        Member findMember = memberJpaRepository.find(savedMember.getId());
        //then
        assertThat(member.getId()).isEqualTo(findMember.getId());
        assertThat(member.getUsername()).isEqualTo(findMember.getUsername());
        assertThat(member).isEqualTo(findMember);//entity 는 같은 id값을 가진 레코드일 경우, 인스턴스도 같다
    }

    @Test
    void memberDeleteTest() {
        //given
        Member memberA = Member.builder().username("usernameA").build();
        Member savedMemberA = memberJpaRepository.save(memberA);
        //when
        memberJpaRepository.delete(savedMemberA);
        Member findMember = memberJpaRepository.find(savedMemberA.getId());
        //then
        assertThat(findMember).isNull();
    }

    @Test
    void memberFindAllTest() {
        //given
        List<Member> expected = Arrays.asList(savedMemberA, savedMemberB, savedMemberC);
        //when
        List<Member> actual = memberJpaRepository.findAll();
        //then
        assertThat(actual).containsExactlyElementsOf(expected);
    }

    @Test
    void memberFindByIdTest() {
        //given
        //when
        Optional<Member> actual = memberJpaRepository.findById(memberA.getId());
        //then
        assertThat(actual).isEqualTo(Optional.ofNullable(memberA));
    }

    @Test
    void memberFindByIdNotFoundTest() {
        //given
        //when
        Optional<Member> actual = memberJpaRepository.findById(100L);
        //then
        assertThat(actual).isEqualTo(Optional.empty());
    }

    @Test
    void memberCountTest() {
        //given
        Long expected = 3L;
        //when
        Long actual = memberJpaRepository.count();
        //then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void memberFindByUsernameAndAgeGreaterThanTest() {
        //given
        List<Member> expected = Arrays.asList(memberB);
        //when
        List<Member> actual = memberJpaRepository.findByUsernameAndAgeGreaterThan(memberB.getUsername(), 15);
        //then
        assertThat(actual).containsExactlyElementsOf(expected);
    }

    @Test
    void memberNamedQueryTest() {
        //given
        Member mem1 = Member.builder().username("AA").age(12).build();
        Member mem2 = Member.builder().username("AA").age(33).build();
        memberJpaRepository.save(mem1);
        memberJpaRepository.save(mem2);
        List<Member> expected = Arrays.asList(mem1, mem2);
        //when
        List<Member> actual = memberJpaRepository.findByUsername("AA");
        //then
        assertThat(actual).containsExactlyElementsOf(expected);
    }

    @Test
    void findPageByAgeTest() {
        //given
        int offset = 1;
        int limit = 3;
        int age = 10;
        Member mem1 = Member.builder().username("mem1").age(10).build();
        Member mem2 = Member.builder().username("mem2").age(10).build();
        Member mem3 = Member.builder().username("mem3").age(10).build();
        Member mem4 = Member.builder().username("mem4").age(10).build();
        Member mem5 = Member.builder().username("mem5").age(10).build();
        memberJpaRepository.save(mem1);
        memberJpaRepository.save(mem2);
        memberJpaRepository.save(mem3);
        memberJpaRepository.save(mem4);
        memberJpaRepository.save(mem5);
        List<Member> expected = Arrays.asList(mem5, mem4, mem3);
        long expectedTotalCount = Arrays.asList(memberA, mem5, mem4, mem3, mem2, mem1).stream().count();

        //when
        List<Member> actual = memberJpaRepository.findPageByAge(age, offset, limit);
        long actualTotalCount = memberJpaRepository.totalCount(age);
        //then
        assertThat(actual).isEqualTo(expected);
        assertThat(actual.size()).isEqualTo(3);
        assertThat(actualTotalCount).isEqualTo(expectedTotalCount);
    }
}
