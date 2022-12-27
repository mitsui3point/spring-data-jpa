package study.datajpa.repository.projection;


import org.springframework.beans.factory.annotation.Value;

public interface UsernameAndAge {
    String getUsername();//close projections

    int getAge();//close projections

//    @Value("#{target.username + ' ' + target.age}")//open projections
//    String getUsernameAndAge();
}
