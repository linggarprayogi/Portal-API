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
@Table(name = "`USERS`", schema = "`MASTER`")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "`ID`")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "`DEPT_MST_ID`")
	private DeptMst deptMst;
	
	@Column(name = "`USERNAME`")
	private String username;
	
	@Column(name = "`PASSWORD`")
	private String password;
	
	@Column(name = "`NAME`")
	private String name;
	
	@Column(name = "`EMAIL`")
	private String email;
	
	@Column(name = "`REG_DT`")
	private Date regDt;
	
	@Column(name = "`REG_EMP`")
	private String regEmp;
	
	@Column(name = "`UPDT_DT`")
	private Date updtDt;
	
	@Column(name = "`UPDT_EMP`")
	private String updtEmp;
}
