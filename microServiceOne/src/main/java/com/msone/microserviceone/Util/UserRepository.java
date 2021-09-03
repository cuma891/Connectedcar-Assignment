package com.msone.microserviceone.Util;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<UserEntity, String> {
     List<UserEntity> findByName(String userName);

   }
