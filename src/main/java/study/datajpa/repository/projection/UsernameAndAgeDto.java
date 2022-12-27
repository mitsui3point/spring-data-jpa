package study.datajpa.repository.projection;

public class UsernameAndAgeDto {
    private final String username;
    private final int age;

    public UsernameAndAgeDto(String username, int age) {
        this.username = username;
        this.age = age;
    }

    public String getUsername() {
        return username;
    }

    public int getAge() {
        return age;
    }
}
