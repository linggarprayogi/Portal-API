package com.glovis.portal.controller;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.glovis.portal.pojo.ApprovalReqPojo;
import com.glovis.portal.pojo.PermitReqPojo;
import com.glovis.portal.pojo.PermitUpdPojo;
import com.glovis.portal.pojo.ResponseModel;
import com.glovis.portal.service.PermitService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@PreAuthorize("isAuthenticated()")
@RequestMapping("/permit")
public class PermitController {

//	private static final Logger    LOG = LoggerFactory.getLogger(PermitController.class);
	
	@Autowired
	private PermitService permitService;
	
	@GetMapping("/data-header")
	public ResponseEntity<?> permitData(@RequestParam(defaultValue = "1") Integer page,
										@RequestParam(defaultValue = "10") Integer limit) {
		log.info("Start : get data header");
		
		try {
			page = page - 1;
			ResponseModel responseModel = permitService.permitDataHeader(page, limit);
			
			return ResponseEntity.status(HttpStatus.OK).body(responseModel);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseModel(false, e.getMessage()));
		} finally {
			log.info("Finish : get data header");
		}
		
	}
	
	@GetMapping("/data-detail-visitor")
	public ResponseEntity<?> permitDataDetailVisitor(@RequestParam Long idPermitHeader,
													 @RequestParam(defaultValue = "1") Integer page,
			             							 @RequestParam(defaultValue = "10") Integer limit) {
		try {
			page = page - 1;
			ResponseModel responseModel = permitService.permitDataDetailVisitor(idPermitHeader, page, limit);
			
			return ResponseEntity.status(HttpStatus.OK).body(responseModel);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseModel(false, e.getMessage()));
		}
	}
	
	@GetMapping("/data-detail-goods")
	public ResponseEntity<?> permitDataDetailGoods(@RequestParam Long idPermitHeader,
												   @RequestParam(defaultValue = "1") Integer page,
												   @RequestParam(defaultValue = "10") Integer limit) {
		try {
			page = page - 1;
			ResponseModel responseModel = permitService.permitDataDetailGoods(idPermitHeader, page, limit);
			
			return ResponseEntity.status(HttpStatus.OK).body(responseModel);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseModel(false, e.getMessage()));
		}
	}
	
	@PostMapping("/request")
	public ResponseEntity<?> permitRequest(@RequestBody PermitReqPojo payload) {
		try {
			
			permitService.permitRequest(payload);
			
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseModel(true, "Success", null));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseModel(false, e.getMessage()));
		}
	}
	
	@PostMapping("/update")
	public ResponseEntity<?> permitUpdate(@RequestBody PermitUpdPojo payload) {
		try {
			
			ResponseModel responseModel = permitService.permitUpdate(payload);
			
			return ResponseEntity.status(HttpStatus.OK).body(responseModel);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseModel(false, e.getMessage()));
		}
	}
	
	@PostMapping("/approval")
	public ResponseEntity<?> permitApproval(@RequestBody ApprovalReqPojo payload) {
		try {
			
			ResponseModel responseModel = permitService.permitApproval(payload);
			
			return ResponseEntity.status(HttpStatus.OK).body(responseModel);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseModel(false, e.getMessage()));
		}
	}
	
	@GetMapping("/validation/{uuid}")
	public ResponseEntity<?> permitValidation(@PathVariable String uuid) {
		try {
			
			ResponseModel responseModel = permitService.permitValidation(uuid);
			
			return ResponseEntity.status(HttpStatus.OK).body(responseModel);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseModel(false, e.getMessage()));
		}
	}
	
//	@GetMapping("/redirect-validation/{uuid}")
//	public ModelAndView permitRedirectValidation(@PathVariable String uuid) {
//		try {
//			
//			String url = permitService.permitRedirectValidation(uuid);
//			
//			return new ModelAndView("redirect:" + url);
//		} catch (Exception e) {
//			String projectUrl = "http://localhost:3000/validation/permit-nok.html";
//			return new ModelAndView("redirect:" + projectUrl);
//		}
//	}
	
	@GetMapping("/stamp-qr")
	public ResponseEntity<?> permitStampQr(HttpServletResponse response,
										   @RequestParam String uuid) {
		try {
			
			ByteArrayInputStream byteArrayInputStream = permitService.permitStampQr(uuid);
		
			response.setContentType("application/pdf");
			response.setHeader("Content-Disposition", "attachment; filename=permit.pdf");
			
			IOUtils.copy(byteArrayInputStream, response.getOutputStream());
			
			return null;
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseModel(false, e.getMessage()));
		}
	}
	
	@DeleteMapping("/header/{id}")
	public ResponseEntity<?> permitDeleteHeader(@RequestBody List<Long> idPermitHeader) {
		try {
			
			ResponseModel responseModel = permitService.deleteHeader(idPermitHeader);
			
			return ResponseEntity.status(HttpStatus.OK).body(responseModel);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseModel(false, e.getMessage()));
		}
	}
	
	@DeleteMapping("/detail-goods/{id}")
	public ResponseEntity<?> permitDeleteDetailGoods(@RequestBody List<Long> idGoodsDetail) {
		try {
			
			ResponseModel responseModel = permitService.deleteDetailGoods(idGoodsDetail);
			
			return ResponseEntity.status(HttpStatus.OK).body(responseModel);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseModel(false, e.getMessage()));
		}
	}
	
	@DeleteMapping("/detail-visitor/{id}")
	public ResponseEntity<?> permitDeleteDetailVisitor(@RequestBody List<Long> idVisitorDetail) {
		try {
			
			ResponseModel responseModel = permitService.deleteDetailVisitor(idVisitorDetail);
			
			return ResponseEntity.status(HttpStatus.OK).body(responseModel);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseModel(false, e.getMessage()));
		}
	}
	
	@PostMapping("/upload-file")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        try {
        	
        	ResponseModel responseModel = permitService.permitUploadTemplate(multipartFile);
        	
        	return ResponseEntity.status(HttpStatus.OK).body(responseModel);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseModel(false, e.getMessage()));
		}
    }
}
