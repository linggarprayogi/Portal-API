package com.glovis.portal.entity;

import java.io.Serializable;
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
@Table(name = "`REQ_PERMIT_GOODS_DETAIL`", schema = "`PERMIT`")
public class ReqPermitGoodsDetail {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "`ID`")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "`REQ_PERMIT_HD_ID`")
	private ReqPermitHd reqPermitHd;
	
	@Column(name = "`ITEM_NAME`")
	private String itemName;
	
	@Column(name = "`QTY`")
	private Integer qty;
	
	@Column(name = "`UNIT_NAME`")
	private String unitName;
}
