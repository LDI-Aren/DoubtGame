package com.bg.doubt.user;

import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@ToString
@Table(name="users")
public class UserEntity {
    @Id
    @Column(name="user_id")
    private String UserId;

    @Column(name="user_name")
    private String Username;

    @NotNull
    @Column(name="user_password")
    private String UserPassword;

    @Column(name="created")
    private LocalDateTime Created;
}
