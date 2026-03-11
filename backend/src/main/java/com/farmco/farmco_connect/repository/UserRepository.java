package com.farmco.farmco_connect.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.farmco.farmco_connect.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Boolean existsByUsername(String username);

    User findByUsernameAndPassword(String username, String password);

    @Query("""
            select u from User u
            join u.location v
            left join v.parent c
            left join c.parent s
            left join s.parent d
            left join d.parent p
            where lower(v.code) = lower(?1) or lower(v.name) = lower(?1)
               or lower(c.code) = lower(?1) or lower(c.name) = lower(?1)
               or lower(s.code) = lower(?1) or lower(s.name) = lower(?1)
               or lower(d.code) = lower(?1) or lower(d.name) = lower(?1)
               or lower(p.code) = lower(?1) or lower(p.name) = lower(?1)
            """)
    List<User> findByAnyLocationLevelCodeOrName(String value);
}
