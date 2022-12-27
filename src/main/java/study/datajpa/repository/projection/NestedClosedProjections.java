package study.datajpa.repository.projection;

public interface NestedClosedProjections {
    String getUsername();
    TeamInfo getTeam();
    interface TeamInfo {
        String getName();
    }
}
