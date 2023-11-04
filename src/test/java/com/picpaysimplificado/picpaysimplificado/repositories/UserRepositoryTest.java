package com.picpaysimplificado.picpaysimplificado.repositories;

import com.picpaysimplificado.picpaysimplificado.domain.user.User;
import com.picpaysimplificado.picpaysimplificado.domain.user.UserType;
import com.picpaysimplificado.picpaysimplificado.dtos.UserDto;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import java.math.BigDecimal;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    UserRepository repository;

    @Autowired
    EntityManager entityManager;
    @Test
    @DisplayName("Should get User successfully from DB")
    void findUserByDocumentSuccess() {
        String document = "99999999901";
        UserDto data = new UserDto("giordan", "teste", document, new BigDecimal(10), "giordan@gmail.com", "232323", UserType.COMMON);
        this.createUser(data);

        Optional<User> result = this.repository.findUserByDocument(document);

        assertThat(result.isPresent()).isTrue();
    }

    @Test
    @DisplayName("Should not get User succesfully from DB when User not exists")
    void findUserByDocumentFail(){
        String document = "99999999901";

        Optional<User> result = this.repository.findUserByDocument(document);

        assertThat(result.isEmpty()).isTrue();
    }

    private User createUser(UserDto userDto){
        User obj = new User(userDto);
        this.entityManager.persist(obj);
        return obj;
    }

}