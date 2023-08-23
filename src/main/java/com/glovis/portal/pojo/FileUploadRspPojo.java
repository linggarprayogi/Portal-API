package com.glovis.portal.pojo;

import lombok.Data;

@Data
public class FileUploadRspPojo {
	private String fileName;
    private String uploadUri;
    private long size;
}
