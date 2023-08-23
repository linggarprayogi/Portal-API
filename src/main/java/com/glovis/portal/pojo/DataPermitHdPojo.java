package com.glovis.portal.pojo;

import com.glovis.portal.entity.DeptMst;
import com.glovis.portal.pojo.DataGoodsDetailPojo.DataGoodsDetailPojoBuilder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataPermitHdPojo {

	private String uuid;
	private String company;
	private DeptMst deptMst;
	private String permitType;
	private String purpose;
	private Integer scanCount;
	private String startDt;
	private String endDt;
}
