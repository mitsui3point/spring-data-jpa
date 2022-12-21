package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
//@Rollback(value = false)
public class MemberJpaRepositoryTest {
    @Autowired
    private MemberJpaRepository memberJpaRepository;

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
        Member savedMember = memberJpaRepository.save(memberA);
        //when
        memberJpaRepository.delete(savedMember);
        Member findMember = memberJpaRepository.find(savedMember.getId());
        //then
        assertThat(findMember).isNull();
    }

    @Test
    void memberFindAllTest() {
        //given
        Member memberA = Member.builder().username("usernameA").build();
        Member memberB = Member.builder().username("usernameB").build();
        Member memberC = Member.builder().username("usernameC").build();
        Member savedMemberA = memberJpaRepository.save(memberA);
        Member savedMemberB = memberJpaRepository.save(memberB);
        Member savedMemberC = memberJpaRepository.save(memberC);
        Member[] expected = {savedMemberA, savedMemberB, savedMemberC};
        //when
        List<Member> actual = memberJpaRepository.findAll();
        //then
        assertThat(actual).containsExactly(expected);
    }

    @Test
    void memberFindByIdTest() {
        //given
        Member memberA = Member.builder().username("usernameA").build();
        Member memberB = Member.builder().username("usernameB").build();
        Member memberC = Member.builder().username("usernameC").build();
        Member savedMemberA = memberJpaRepository.save(memberA);
        Member savedMemberB = memberJpaRepository.save(memberB);
        Member savedMemberC = memberJpaRepository.save(memberC);
        //when
        Optional<Member> actual = memberJpaRepository.findById(memberA.getId());
        //then
        assertThat(actual).isEqualTo(Optional.ofNullable(memberA));
    }

    @Test
    void memberFindByIdNotFoundTest() {
        //given
        Member memberA = Member.builder().username("usernameA").build();
        Member memberB = Member.builder().username("usernameB").build();
        Member memberC = Member.builder().username("usernameC").build();
        Member savedMemberA = memberJpaRepository.save(memberA);
        Member savedMemberB = memberJpaRepository.save(memberB);
        Member savedMemberC = memberJpaRepository.save(memberC);
        //when
        Optional<Member> actual = memberJpaRepository.findById(100L);
        //then
        assertThat(actual).isEqualTo(Optional.empty());
    }

    @Test
    void memberCountTest() {
        //given
        Member memberA = Member.builder().username("usernameA").build();
        Member memberB = Member.builder().username("usernameB").build();
        Member memberC = Member.builder().username("usernameC").build();
        Member savedMemberA = memberJpaRepository.save(memberA);
        Member savedMemberB = memberJpaRepository.save(memberB);
        Member savedMemberC = memberJpaRepository.save(memberC);
        Long expected = 3L;
        //when
        Long actual = memberJpaRepository.count();
        //then
        assertThat(actual).isEqualTo(expected);
    }
}
