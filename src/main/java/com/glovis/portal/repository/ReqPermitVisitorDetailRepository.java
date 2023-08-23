package com.glovis.portal.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.glovis.portal.entity.ReqPermitHd;
import com.glovis.portal.entity.ReqPermitVisitorDetail;

@Repository
public interface ReqPermitVisitorDetailRepository extends JpaRepository<ReqPermitVisitorDetail, Long>{

	Optional<ReqPermitVisitorDetail> findByReqPermitHd(ReqPermitHd reqPermitHd);
	Page<ReqPermitVisitorDetail> findByReqPermitHd(ReqPermitHd reqPermitHd, Pageable pageable);
	List<ReqPermitVisitorDetail> findByReqPermitHdIn(List<ReqPermitHd> reqPermitHds);
}