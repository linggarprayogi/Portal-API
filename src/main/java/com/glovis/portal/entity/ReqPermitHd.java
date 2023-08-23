package com.glovis.portal.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "`REQ_PERMIT_HD`", schema = "`PERMIT`")
public class ReqPermitHd {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "`ID`")
	private Long id;
	
	@Column(name = "`UUID`")
	private String uuid;
	
	@Column(name = "`COMPANY`")
	private String company;
	
	@ManyToOne
	@JoinColumn(name = "`DEPT_MST_ID`")
	private DeptMst deptMst;
	
	@Column(name = "`PERMIT_TYPE`")
	private String permitType;
	
	@Column(name = "`PURPOSE`")
	private String purpose;
	
	@Column(name = "`SCAN_COUNT`")
	private Integer scanCount;
	
	@Column(name = "`START_DT`")
	private String startDt;
	
	@Column(name = "`END_DT`")
	private String endDt;
	
	@Column(name = "`REG_DT`")
	private Date regDt;
	
	@Column(name = "`REG_EMP`")
	private String regEmp;
	
	@Column(name = "`UPDT_DT`")
	private Date updtDt;
	
	@Column(name = "`UPDT_EMP`")
	private String updtEmp;
	
}
