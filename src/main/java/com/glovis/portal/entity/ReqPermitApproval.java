package com.glovis.portal.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.glovis.portal.entity.ReqPermitHd.ReqPermitHdBuilder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "`REQ_PERMIT_APPROVAL`", schema = "`PERMIT`")
public class ReqPermitApproval {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "`ID`")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "`REQ_PERMIT_HD_ID`")
	private ReqPermitHd reqPermitHd;
	
	@Column(name = "`APPROVED`")
	private String approved;
	
	@Column(name = "`REG_DT`")
	private Date regDt;
	
	@Column(name = "`REG_EMP`")
	private String regEmp;
	
	@Column(name = "`UPDT_DT`")
	private Date updtDt;
	
	@Column(name = "`UPDT_EMP`")
	private String updtEmp;
}
