package com.glovis.portal.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.glovis.portal.entity.ReqPermitApproval;
import com.glovis.portal.entity.ReqPermitHd;

@Repository
public interface ReqPermitApprovalRepository extends JpaRepository<ReqPermitApproval, Long>{

	Optional<ReqPermitApproval> findByReqPermitHd(ReqPermitHd reqPermitHd);
	List<ReqPermitApproval> findByReqPermitHdIn(List<ReqPermitHd> reqPermitHds);
}
