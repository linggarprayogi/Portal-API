package com.glovis.portal.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.glovis.portal.entity.ReqPermitHd;

@Repository
public interface ReqPermitHdRepository extends JpaRepository<ReqPermitHd, Long>{

	Page<ReqPermitHd> findByRegEmp(String username, Pageable pageable);
	Optional<ReqPermitHd> findByUuid(String uuid);
}
