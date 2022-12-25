package study.datajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import javax.persistence.QueryHint;
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

    //컬렉션: 조회건수가 없어도 빈 컬렉션이 들어감. null 반환되지 않는 메서드 => null check 금지!
    List<Member> findListByUsername(String username);

    //단건: 조회건수가 없으면 null 반환
    Member findMemberByUsername(String username);

    //단건 Optional: 조회건수가 없어도 Optional.empty() 반환. null 반환되지 않는 메서드 => null check 금지!
    Optional<Member> findOptionalByUsername(String username);

    //Page : 추가 count 쿼리 결과를 포함하는 페이징
    Page<Member> findPageByAge(int age, Pageable pageable);

    //Slice : 추가 count 쿼리 없이 다음 페이지만 확인 가능
    Slice<Member> findSliceByAge(int age, Pageable pageable);

    //List : 추가 count 쿼리 없음 paging 공통 메서드 사용 불가능
    List<Member> findListByAge(int age, Pageable pageable);

    //Page : 추가 count 쿼리 결과를 포함하는 페이징, totalCount 부하 튜닝
    @Query(value = "select m from Member m left join m.team t where m.age = :age ",
            countQuery = "select count(m) from Member m where m.age = :age ")
    Page<Member> findMemberAllCountBy(@Param("age") int age, Pageable pageable);

    @Modifying(clearAutomatically = true)//em.clear(); 영속성 컨텍스트 초기화
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age ")
    int bulkAgePlus(@Param("age") int age);

    @Query("select m from Member m left join fetch m.team t ")
    List<Member> findMembersFetchJoin();

    @Override
    @EntityGraph(attributePaths = {"team"})
    List<Member> findAll();

    @Query("select m from Member m ")
    @EntityGraph(attributePaths = {"team"})
    List<Member> findMembersEntityGraph();

    @EntityGraph(attributePaths = {"team"})
    List<Member> findMembersEntityGraphByUsername(@Param("username") String username);

    @EntityGraph("Member.all")
    List<Member> findMembersNamedEntityGraphByUsername(@Param("username") String username);

    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
    Member findReadOnlyByUsername(String username);
}
