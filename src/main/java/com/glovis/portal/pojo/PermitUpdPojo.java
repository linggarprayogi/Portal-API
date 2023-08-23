package com.glovis.portal.pojo;

import java.util.List;

import lombok.Data;

@Data
public class PermitUpdPojo {
	private Long id;
	private String permitType;
	private String company;
	private String purpose;
	private String startDt;
	private String endDt;
	private List<PermitVisitorDetailPojo> permitVisitorDetails;
	private List<PermitGoodsDetailPojo> permitGoodsDetails;
}