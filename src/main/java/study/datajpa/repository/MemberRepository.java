package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    List<Member> findHelloBy();

    List<Member> findFirst2By();

    //@Query(name = "Member.findByUsername")//메서드명이 동일하다면 생략가능
    List<Member> findByUsername(@Param("username") String username);

    //@Query("select m from Member m where m.uservdsvdvname = :username and m.age = :age ")//compile time error
    @Query("select m from Member m where m.username = :username and m.age = :age ")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    @Query("select m.username from Member m ")
    List<String> findUsernameList();

    @Query("select new study.datajpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();

    @Query("select m from Member m where m.username in :names ")
    List<Member> findByNames(@Param("names") List<String> names);

    List<Member> findListByUsername(String username); //컬렉션: 조회건수가 없어도 빈 컬렉션이 들어감. null 반환되지 않는 메서드 => null check 금지!

    Member findMemberByUsername(String username); //단건: 조회건수가 없으면 null 반환

    Optional<Member> findOptionalByUsername(String username); //단건 Optional: 조회건수가 없어도 Optional.empty() 반환. null 반환되지 않는 메서드 => null check 금지!
}
