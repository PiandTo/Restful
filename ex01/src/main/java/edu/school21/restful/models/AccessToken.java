package edu.school21.restful.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "access_token", schema = "restful")
@NoArgsConstructor
@AllArgsConstructor
public class AccessToken{
    @Id
    @GeneratedValue
    private Long id;

    @JsonIgnore
    @CreatedDate
    private Date created;

    @JsonIgnore
    @LastModifiedDate
    private Date updated;
    private String login;
    private String password;
    private boolean isExpired;

    public AccessToken(String login, String password, boolean isExpired) {
        this.login = login;
        this.password = password;
        this.isExpired = isExpired;
    }
}
