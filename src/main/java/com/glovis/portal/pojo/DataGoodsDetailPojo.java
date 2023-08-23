package com.glovis.portal.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataGoodsDetailPojo {

	private String itemName;
	private Integer qty;
	private String unitName;
}
