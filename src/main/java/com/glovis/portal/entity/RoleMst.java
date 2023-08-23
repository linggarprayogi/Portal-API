package com.glovis.portal.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "`ROLE_MST`", schema = "`MASTER`")
public class RoleMst {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "`ID`")
	private Long id;
	
	@Column(name = "`ROLE_NAME`")
	private String roleName;
	
	@Column(name = "`DESCRIPTION`")
	private String description;
	
	@Column(name = "`REG_DT`")
	private Date regDt;
	
	@Column(name = "`REG_EMP`")
	private String regEmp;
	
	@Column(name = "`UPDT_DT`")
	private Date updtDt;
	
	@Column(name = "`UPDT_EMP`")
	private String updtEmp;
	
}
