package hn_152.bookstore.model.entity.user;

import hn_152.bookstore.model.entity.AuditableEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
public class User extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String password;

    private String username;

    private String personName;

    private String phoneNumber;

    private String address;

    private String oauth2Id;

    private Boolean isOauth2User = false;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable
    private List<Role> roles;

}
