package com.example.cordo.repository.jpa;

import com.example.cordo.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<User,String> {

}
