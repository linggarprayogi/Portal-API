package com.glovis.portal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.glovis.portal.entity.RoleMst;

@Repository
public interface RoleMstRepository extends JpaRepository<RoleMst, Long> {

}
