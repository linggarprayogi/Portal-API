package com.glovis.portal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.glovis.portal.entity.DeptMst;

@Repository
public interface DeptMstRepository extends JpaRepository<DeptMst, Long> {

}
