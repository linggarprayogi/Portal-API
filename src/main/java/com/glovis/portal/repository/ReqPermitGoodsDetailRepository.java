package com.glovis.portal.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.glovis.portal.entity.ReqPermitGoodsDetail;
import com.glovis.portal.entity.ReqPermitHd;

@Repository
public interface ReqPermitGoodsDetailRepository extends JpaRepository<ReqPermitGoodsDetail, Long> {

	List<ReqPermitGoodsDetail> findByReqPermitHdIn(List<ReqPermitHd> reqPermitHds);
	Page<ReqPermitGoodsDetail> findByReqPermitHd(ReqPermitHd reqPermitHd, Pageable pageable);
}
