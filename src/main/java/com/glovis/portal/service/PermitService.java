package com.glovis.portal.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.JPEGFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.glovis.portal.entity.DeptMst;
import com.glovis.portal.entity.ReqPermitApproval;
import com.glovis.portal.entity.ReqPermitGoodsDetail;
import com.glovis.portal.entity.ReqPermitHd;
import com.glovis.portal.entity.ReqPermitVisitorDetail;
import com.glovis.portal.entity.User;
import com.glovis.portal.pojo.ApprovalReqPojo;
import com.glovis.portal.pojo.DataGoodsDetailPojo;
import com.glovis.portal.pojo.DataPermitHdPojo;
import com.glovis.portal.pojo.DataVisitorDetailPojo;
import com.glovis.portal.pojo.FileUploadRspPojo;
import com.glovis.portal.pojo.PermitGoodsDetailPojo;
import com.glovis.portal.pojo.PermitReqPojo;
import com.glovis.portal.pojo.PermitUpdPojo;
import com.glovis.portal.pojo.PermitVisitorDetailPojo;
import com.glovis.portal.pojo.ResponseModel;
import com.glovis.portal.repository.ReqPermitApprovalRepository;
import com.glovis.portal.repository.ReqPermitGoodsDetailRepository;
import com.glovis.portal.repository.ReqPermitHdRepository;
import com.glovis.portal.repository.ReqPermitVisitorDetailRepository;
import com.glovis.portal.repository.UserRepository;
import com.glovis.portal.utils.FileUploadUtil;

import net.bytebuddy.utility.RandomString;
import net.glxn.qrgen.core.image.ImageType;
import net.glxn.qrgen.javase.QRCode;

@Service
@Transactional
public class PermitService {

	@Autowired
	private ReqPermitHdRepository reqPermitHdRepository;
	@Autowired
	private ReqPermitGoodsDetailRepository reqPermitGoodsDetailRepository;
	@Autowired
	private ReqPermitVisitorDetailRepository reqPermitVisitorDetailRepository;
	@Autowired
	private ReqPermitApprovalRepository reqPermitApprovalRepository;
	@Autowired
	private UserRepository userRepository;
	@Value("${permit.upload.path}")
	private String uploadPath;
	
	public ResponseModel permitDataHeader(Integer page, Integer limit) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		String username;
		List<String> roles = new ArrayList<String>();
		if (principal instanceof UserDetails) {
		  username = ((UserDetails)principal).getUsername();
		  for (GrantedAuthority a :  ((UserDetails)principal).getAuthorities()) {
				roles.add(a.getAuthority());
			}
		} else {
		  username = principal.toString();
		}
		
		List<ReqPermitHd> content = new ArrayList();
		String[] eligibleRole = {"Manager"};
		if (roles.stream().filter(o -> o.equals(Arrays.stream(eligibleRole).findAny().orElse(null))).findFirst().isPresent()) {
			Pageable paging = PageRequest.of(page, limit);
			Page<ReqPermitHd> reqPermitHd = reqPermitHdRepository.findAll(paging);
			content = reqPermitHd.getContent();
		}else {
			Pageable paging = PageRequest.of(page, limit);
			Page<ReqPermitHd> reqPermitHd = reqPermitHdRepository.findByRegEmp(username, paging);
			content = reqPermitHd.getContent();
		}
		
		List<DataPermitHdPojo> datas = new ArrayList();
		for (ReqPermitHd c : content) {
			DataPermitHdPojo data = DataPermitHdPojo.builder()
					.uuid(c.getUuid())
					.company(c.getCompany())
					.deptMst(c.getDeptMst())
					.permitType(c.getPermitType())
					.purpose(c.getPurpose())
					.scanCount(c.getScanCount())
					.startDt(c.getStartDt())
					.endDt(c.getEndDt())
					.build();
			datas.add(data);
		}
		
		
		return new ResponseModel(true, "Success", datas);
	}
	
	public ResponseModel permitDataDetailVisitor(Long idPermitHeader, Integer page, Integer limit) {
		Pageable paging = PageRequest.of(page, limit);
		
		ReqPermitHd permitHd = reqPermitHdRepository.getById(idPermitHeader);
		
		Page<ReqPermitVisitorDetail> permitVisitorDetail = reqPermitVisitorDetailRepository.findByReqPermitHd(permitHd, paging);
		List<ReqPermitVisitorDetail> content = permitVisitorDetail.getContent();
		
		List<DataVisitorDetailPojo> datas = new ArrayList();
		for (ReqPermitVisitorDetail c : content) {
			DataVisitorDetailPojo data = DataVisitorDetailPojo.builder()
					.visitorName(c.getVisitorName())
					.build();
			datas.add(data);
		}
		
		return new ResponseModel(true, "Success", datas);
	}
	
	public ResponseModel permitDataDetailGoods(Long idPermitHeader, Integer page, Integer limit) {
		Pageable paging = PageRequest.of(page, limit);
		
		ReqPermitHd permitHd = reqPermitHdRepository.getById(idPermitHeader);
		
		Page<ReqPermitGoodsDetail> permitGoodsDetail = reqPermitGoodsDetailRepository.findByReqPermitHd(permitHd, paging);
		List<ReqPermitGoodsDetail> content = permitGoodsDetail.getContent();
		
		List<DataGoodsDetailPojo> datas = new ArrayList();
		for (ReqPermitGoodsDetail c : content) {
			DataGoodsDetailPojo data = DataGoodsDetailPojo.builder()
					.itemName(c.getItemName())
					.qty(c.getQty())
					.unitName(c.getUnitName())
					.build();
			datas.add(data);
		}
		
		return new ResponseModel(true, "Success", datas);
	}
	
	public void permitRequest(PermitReqPojo payload) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		String username;
		if (principal instanceof UserDetails) {
		  username = ((UserDetails)principal).getUsername();
		} else {
		  username = principal.toString();
		}
		
		User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
		
		
		/* Permit Header*/
		DeptMst departement = user.getDeptMst();
		String company = payload.getCompany() == null ? "" : payload.getCompany();
		String permitTpe = payload.getPermitType() == null ? "" : payload.getPermitType();
		String purpose = payload.getPurpose() == null ? "" : payload.getPurpose();
		String startDate = payload.getStartDt() == null ? "" : payload.getStartDt();
		String endDate = payload.getPermitType() == null ? "" : payload.getEndDt();
		
		String uuid = RandomStringUtils.randomAlphanumeric(4);
		ReqPermitHd permitHd = ReqPermitHd.builder()
				.uuid(uuid)
				.company(company)
				.permitType(permitTpe)
				.deptMst(departement)
				.purpose(purpose)
				.startDt(startDate)
				.endDt(endDate)
				.scanCount(0)
				.regDt(new Timestamp(System.currentTimeMillis()))
				.regEmp(username)
				.build();
		reqPermitHdRepository.save(permitHd);
		
		/* Detail : Permit Visitor */
		if (payload.getPermitVisitorDetails() != null) {
			List<ReqPermitVisitorDetail> visitorDetails = new ArrayList<ReqPermitVisitorDetail>();
			for (PermitVisitorDetailPojo pvd : payload.getPermitVisitorDetails()) {
				String visitorName = pvd.getVisitorName();
				ReqPermitVisitorDetail visitorDetail = ReqPermitVisitorDetail.builder()
						.reqPermitHd(permitHd)
						.visitorName(visitorName)
						.build();
				visitorDetails.add(visitorDetail);
			}
			
			
			reqPermitVisitorDetailRepository.saveAll(visitorDetails);
		}
		
		/* Detail : Permit Goods */
		if (payload.getPermitGoodsDetails() != null) {
			List<ReqPermitGoodsDetail> goodsDetails = new ArrayList<ReqPermitGoodsDetail>();
			for (PermitGoodsDetailPojo rpg : payload.getPermitGoodsDetails()) {
				String itemName = rpg.getItemName();
				Integer qty = rpg.getQty();
				String unitName = rpg.getUnitName();
				
				ReqPermitGoodsDetail goodsDetail = ReqPermitGoodsDetail.builder()
						.reqPermitHd(permitHd)
						.itemName(itemName)
						.qty(qty)
						.unitName(unitName)
						.build();
				goodsDetails.add(goodsDetail);
			}
			reqPermitGoodsDetailRepository.saveAll(goodsDetails);
		}
	}
	
	public ResponseModel permitUpdate(PermitUpdPojo payload) {
		ResponseModel responseModel = new ResponseModel(true, "Success", null);
		
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		String username;
		if (principal instanceof UserDetails) {
		  username = ((UserDetails)principal).getUsername();
		} else {
		  username = principal.toString();
		}
		
		User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
		DeptMst departement = user.getDeptMst();
		
		Long id = payload.getId() == null ? null : payload.getId();
		ReqPermitHd reqPermitHd = reqPermitHdRepository.findById(id).orElse(null);
		
		ReqPermitApproval approval = reqPermitApprovalRepository.findByReqPermitHd(reqPermitHd).orElse(null);
		
		if (approval != null && approval.getApproved().equals("Y")) return new ResponseModel(false, "Request has approved", null);
		
		/* Permit Header*/
		String company = payload.getCompany() == null ? reqPermitHd.getCompany() : payload.getCompany();
		String permitTpe = payload.getPermitType() == null ? reqPermitHd.getPermitType() : payload.getPermitType();
		String purpose = payload.getPurpose() == null ? reqPermitHd.getPurpose() : payload.getPurpose();
		String startDate = payload.getStartDt() == null ? reqPermitHd.getStartDt() : payload.getStartDt();
		String endDate = payload.getPermitType() == null ? reqPermitHd.getEndDt() : payload.getEndDt();
		Date   regDt = reqPermitHd.getRegDt();
		String regEmp = reqPermitHd.getRegEmp();
				
		ReqPermitHd permitHd = ReqPermitHd.builder()
				.id(id)
				.company(company)
				.permitType(permitTpe)
				.deptMst(departement)
				.purpose(purpose)
				.startDt(startDate)
				.endDt(endDate)
				.regDt(regDt)
				.regEmp(regEmp)
				.updtDt(new Timestamp(System.currentTimeMillis()))
				.updtEmp(username)
				.build();
		reqPermitHdRepository.save(permitHd);
		
		/* Detail : Permit Visitor */
		if (payload.getPermitVisitorDetails() != null) {
			List<ReqPermitVisitorDetail> visitorDetails = new ArrayList<ReqPermitVisitorDetail>();
			for (PermitVisitorDetailPojo pvd : payload.getPermitVisitorDetails()) {
				Long idPvd = pvd.getId();
				String visitorName = pvd.getVisitorName();
				ReqPermitVisitorDetail visitorDetail = ReqPermitVisitorDetail.builder()
						.id(idPvd)
						.reqPermitHd(permitHd)
						.visitorName(visitorName)
						.build();
				visitorDetails.add(visitorDetail);
			}
			
			
			reqPermitVisitorDetailRepository.saveAll(visitorDetails);
		}
		
		/* Detail : Permit Goods */
		if (payload.getPermitGoodsDetails() != null) {
			List<ReqPermitGoodsDetail> goodsDetails = new ArrayList<ReqPermitGoodsDetail>();
			for (PermitGoodsDetailPojo pgd : payload.getPermitGoodsDetails()) {
				Long idPgd = pgd.getId();
				String itemName = pgd.getItemName();
				Integer qty = pgd.getQty();
				String unitName = pgd.getUnitName();
				
				ReqPermitGoodsDetail goodsDetail = ReqPermitGoodsDetail.builder()
						.id(idPgd)
						.reqPermitHd(permitHd)
						.itemName(itemName)
						.qty(qty)
						.unitName(unitName)
						.build();
				goodsDetails.add(goodsDetail);
			}
			reqPermitGoodsDetailRepository.saveAll(goodsDetails);
		}
		
		return responseModel;
	}
	
	public ResponseModel permitApproval(ApprovalReqPojo payload) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		String username;
		List<String> roles = new ArrayList<String>();
		if (principal instanceof UserDetails) {
		  username = ((UserDetails)principal).getUsername();
		  for (GrantedAuthority a :  ((UserDetails)principal).getAuthorities()) {
				roles.add(a.getAuthority());
			}
		} else {
		  username = principal.toString();
		}
		
		String[] eligibleRole = {"Manager"};
		if (!roles.stream().filter(o -> o.equals(Arrays.stream(eligibleRole).findAny().orElse(null))).findFirst().isPresent()) {
			return new ResponseModel(false, "Role not allow to approve", null);
		}
		
		List<Long> idPermitHeaders = payload.getIdPermitHeaders();
		
		List<ReqPermitHd> reqPermitHds = reqPermitHdRepository.findAllById(idPermitHeaders);
		
		List<ReqPermitApproval> reqPermitApprovals = new ArrayList<ReqPermitApproval>();
		for (ReqPermitHd rph : reqPermitHds) {
			ReqPermitApproval reqPermitApproval = ReqPermitApproval.builder()
					.reqPermitHd(rph)
					.approved("Y")
					.regDt(new Timestamp(System.currentTimeMillis()))
					.regEmp(username)
					.build();
			reqPermitApprovals.add(reqPermitApproval);
		}
		
		reqPermitApprovalRepository.saveAll(reqPermitApprovals);
		
		return new ResponseModel(true, "Success", null);
	}
	
	public ResponseModel permitValidation(String uuid) throws ParseException {
		ResponseModel responseModel = new ResponseModel(true, "Success", null);
		
		// get data header by uuid
		ReqPermitHd permitHd = reqPermitHdRepository.findByUuid(uuid).orElse(null);
		
		if (permitHd == null) {
			return new ResponseModel(false, "Invalid : Permit not found", null);
		}
		
		permitHd.setScanCount(permitHd.getScanCount() + 1);
		reqPermitHdRepository.save(permitHd);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date endDate = sdf.parse(permitHd.getEndDt());
		Date currentDate = new Date();
		if (endDate.before(currentDate)) {
			return new ResponseModel(false, "Invalid : Permit expired", null);
		}
		
		// get data approval by idheader and check approval Y or N
		ReqPermitApproval approval = reqPermitApprovalRepository.findByReqPermitHd(permitHd).orElse(null);
		
		if (approval == null || (approval != null && approval.getApproved().equals("N"))) {
			return new ResponseModel(false, "Invalid : Permit not approved", null);
		}
		
		return responseModel;
	}
	
//	public String permitRedirectValidation(String uuid) throws ParseException {
//		
//		// get data header by uuid
//		ReqPermitHd permitHd = reqPermitHdRepository.findByUuid(uuid).orElse(null);
//		
//		if (permitHd == null) {
//			//return new ResponseModel(false, "Invalid : Permit not found", null);
//			return "http://localhost:3000/validation/permit-nok.html";
//		}
//		
//		permitHd.setScanCount(permitHd.getScanCount() + 1);
//		reqPermitHdRepository.save(permitHd);
//		
//		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
//		Date endDate = sdf.parse(permitHd.getEndDt());
//		Date currentDate = new Date();
//		if (endDate.before(currentDate)) {
//			//return new ResponseModel(false, "Invalid : Permit expired", null);
//			return "http://localhost:3000/validation/permit-nok.html";
//		}
//		
//		// get data approval by idheader and check approval Y or N
//		ReqPermitApproval approval = reqPermitApprovalRepository.findByReqPermitHd(permitHd).orElse(null);
//		
//		if (approval == null || (approval != null && approval.getApproved().equals("N"))) {
//			//return new ResponseModel(false, "Invalid : Permit not approved", null);
//			return "http://localhost:3000/validation/permit-nok.html";
//		}
//		
//		return "http://localhost:3000/validation/permit-ok.html";
//	}
	
	public ByteArrayInputStream permitStampQr(String uuid) throws ParseException, IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		String filePath = "D:/Temp/Output/in/permit.pdf";
		String urlBarcode = "http://localhost:9090/permit/validation/" + uuid;
		try(InputStream inputStream = Files.newInputStream(Paths.get(filePath)); PDDocument document = PDDocument.load(inputStream)) {
			
			for (PDPage page : document.getPages()) {
				addQrCode(document, page, urlBarcode, 40, 40);
			}
			
			document.save(baos);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			baos.close();
		}
		
		return new ByteArrayInputStream(baos.toByteArray());
		
	}
	
	private static void addQrCode(PDDocument document, PDPage page, String text, float x, float y) {
		try (PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true)) {
			try(FileInputStream is = new FileInputStream(QRCode.from(text).to(ImageType.JPG).file())) {
				PDImageXObject image = JPEGFactory.createFromStream(document, is);
				image.setWidth(70);
				image.setHeight(70);
				contentStream.drawImage(image, x, y, 70, 70);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ResponseModel deleteHeader(List<Long> idPermitHeader) {
		
		List<ReqPermitHd> reqPermitHds = reqPermitHdRepository.findAllById(idPermitHeader);
		List<ReqPermitApproval> approvals = reqPermitApprovalRepository.findByReqPermitHdIn(reqPermitHds);
		
		StringBuilder msg = new StringBuilder();
		for (ReqPermitApproval rpa : approvals) {
			if (rpa != null && rpa.getApproved().equals("Y")) {
				msg.append("Can not delete : Request header " + rpa.getReqPermitHd().getId() + " has approved");
			}
		}
		
		if (msg.toString() != "") {
			return new ResponseModel(false, msg.toString(), null);
		} else {
			List<ReqPermitVisitorDetail> reqPermitVisitorDetails = reqPermitVisitorDetailRepository.findByReqPermitHdIn(reqPermitHds);
			List<ReqPermitGoodsDetail> reqPermitGoodsDetails = reqPermitGoodsDetailRepository.findByReqPermitHdIn(reqPermitHds);
			
			Boolean detailIsDeleted = false;
			if (!reqPermitVisitorDetails.isEmpty()) {
				List<Long> pvdIds = new ArrayList<Long>();
				for (ReqPermitVisitorDetail pvd : reqPermitVisitorDetails) {
					pvdIds.add(pvd.getId());
				}
				reqPermitVisitorDetailRepository.deleteAllById(pvdIds);
				detailIsDeleted = true;
			}
			
			if (!reqPermitGoodsDetails.isEmpty()) {
				List<Long> pgdIds = new ArrayList<Long>();
				for (ReqPermitGoodsDetail pgd : reqPermitGoodsDetails) {
					pgdIds.add(pgd.getId());
				}
				reqPermitGoodsDetailRepository.deleteAllById(pgdIds);
				detailIsDeleted = true;
			}

			if (detailIsDeleted) {
				reqPermitHdRepository.deleteAllById(idPermitHeader);
			}
		}
			
		return new ResponseModel(true, "Success", null);
		
	}
	
	public ResponseModel deleteDetailVisitor(List<Long> idVisitorDetail) {
		List<ReqPermitVisitorDetail> reqPermitVisitorDetails = reqPermitVisitorDetailRepository.findAllById(idVisitorDetail);
		
		List<ReqPermitHd> reqPermitHds = new ArrayList<ReqPermitHd>();
		for (ReqPermitVisitorDetail pvd : reqPermitVisitorDetails) {
			reqPermitHds.add(pvd.getReqPermitHd());
		}
		
		List<ReqPermitApproval> approvals = reqPermitApprovalRepository.findByReqPermitHdIn(reqPermitHds);
		
		StringBuilder msg = new StringBuilder();
		for (ReqPermitApproval rpa : approvals) {
			if (rpa != null && rpa.getApproved().equals("Y")) {
				msg.append("Can not delete : Request header " + rpa.getReqPermitHd().getId() + " has approved");
			}
		}
		
		if (msg.toString() != "") {
			return new ResponseModel(false, msg.toString(), null);
		} else {
			reqPermitVisitorDetailRepository.deleteAllById(idVisitorDetail);
		}
		
		return new ResponseModel(true, "Success", null);
	}
	
	public ResponseModel deleteDetailGoods(List<Long> idGoodsDetail) {
		List<ReqPermitGoodsDetail> reqPermitGoodsDetails = reqPermitGoodsDetailRepository.findAllById(idGoodsDetail);
		
		List<ReqPermitHd> reqPermitHds = new ArrayList<ReqPermitHd>();
		for (ReqPermitGoodsDetail pgd : reqPermitGoodsDetails) {
			reqPermitHds.add(pgd.getReqPermitHd());
		}
		
		List<ReqPermitApproval> approvals = reqPermitApprovalRepository.findByReqPermitHdIn(reqPermitHds);
		
		StringBuilder msg = new StringBuilder();
		for (ReqPermitApproval rpa : approvals) {
			if (rpa != null && rpa.getApproved().equals("Y")) {
				msg.append("Can not delete : Request header " + rpa.getReqPermitHd().getId() + " has approved");
			}
		}
		
		if (msg.toString() != "") {
			return new ResponseModel(false, msg.toString(), null);
		} else {
			reqPermitGoodsDetailRepository.deleteAllById(idGoodsDetail);
		}
		
		return new ResponseModel(true, "Success", null);
	}
	
	public ResponseModel permitUploadTemplate(MultipartFile multipartFile) throws IOException {
		String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        long size = multipartFile.getSize();
        
        String uploadUri = FileUploadUtil.saveFile(fileName, multipartFile, uploadPath);
         
        FileUploadRspPojo data = new FileUploadRspPojo();
        data.setFileName(fileName);
        data.setSize(size);
        data.setUploadUri(uploadUri);
        
		return new ResponseModel(true, "Success", data);
	}
	
}
