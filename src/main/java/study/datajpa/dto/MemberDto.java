package study.datajpa.dto;

import lombok.*;
import study.datajpa.entity.Member;

import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "username", "teamName"})
public class MemberDto {
    private Long id;
    private String username;
    private String teamName;

    public MemberDto(Long id, String username, String teamName) {
        this.id = id;
        this.username = username;
        this.teamName = teamName;
    }

    @Builder
    private MemberDto(Member member) {
        this.id = member.getId();
        this.username = member.getUsername();
        this.teamName = member.getTeam().getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemberDto memberDto = (MemberDto) o;
        return Objects.equals(getId(), memberDto.getId()) && Objects.equals(getUsername(), memberDto.getUsername()) && Objects.equals(getTeamName(), memberDto.getTeamName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUsername(), getTeamName());
    }
}
