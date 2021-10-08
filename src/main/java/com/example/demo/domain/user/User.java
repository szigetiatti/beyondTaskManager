package com.example.demo.domain.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

/**
 * @author szigeti.attila96@gmail.com
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Slf4j
@Table(name = "BEYOND_USER",
    indexes = {
        @Index(name = "BEYOND_USER_U1", columnList = "USER_ID", unique = true),
        @Index(name = "BEYOND_USER_U2", columnList = "USERNAME", unique = true)
    })
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "USER_ID", unique = true, nullable = false)
  private Long id;

  @Column(name = "USERNAME", nullable = false, length = 240)
  private String userName;

  @Column(name = "FIRST_NAME", length = 240)
  private String firstName;

  @Column(name = "LAST_NAME", length = 240)
  private String lastName;

  private User(UserBuilder userBuilder) {
    this.userName = userBuilder.userName;
    this.firstName = userBuilder.firstName;
    this.lastName = userBuilder.lastName;
  }

  public boolean merge(User other) {
    this.firstName = StringUtils.defaultIfBlank(other.getFirstName(), this.firstName);
    this.lastName = StringUtils.defaultIfBlank(other.getLastName(), this.lastName);
    return true;
  }

  public static class UserBuilder {

    public User build() {
      Validate.notBlank(this.userName, "username must not be blank");

      return new User(this);
    }
  }
}
