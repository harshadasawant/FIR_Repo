package com.police.fir.bean;

import com.police.fir.entity.ExceptionDBConnect;
import com.police.fir.entity.FirDetail;
import com.police.fir.entity.PoliceStation;
import com.police.fir.repository.ExceptionRepository;
import com.police.fir.repository.FirDetailRepository;
import com.police.fir.repository.PoliceStationRepository;
import com.police.fir.service.FIRSearchService;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestTemplate;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class PoliceStationIdMapper {
    @Autowired
    PoliceStationRepository policeStationRepository;

    @Autowired
    FirDetailRepository firDetailRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ExceptionDBConnect exceptionDBConnect;

    @Autowired
    private ExceptionRepository exceptionRepository;

    Logger logger = LoggerFactory.getLogger(PoliceStationIdMapper.class);

    public void beanToDBMapper(PoliceStationResponseBean policeStationResponseBean, int districtId) {
        List<List<String>> rows = policeStationResponseBean.getRows();
        if(rows != null) {
           logger.info(" district Id : "+districtId+ " size of police station : " +rows.size());
        } else{
            logger.info(" No data found for district Id : "+districtId);
        }
        for (List<String> station : rows) {
            PoliceStation policeStation = new PoliceStation();
            System.out.println(station);
            policeStation.setPolicestationId(Integer.parseInt(station.get(0)));
            policeStation.setDistrictId(districtId);
            policeStation.setPolicestationName(station.get(1).toString());
            policeStationRepository.save(policeStation);
        }
    }

    public void beanToFIrDetailsDBMapper(FIRSearchBean firSeachBean) {
        String regNo =null;
        CitizenFirSearchBean citizenFirSearchBean = firSeachBean.getCitizenFirSearchBean();
        System.out.println(citizenFirSearchBean);
        List<CitizenFirSearchBean> citizenFirSearchBeans = firSeachBean.getList();
        if(citizenFirSearchBeans !=null) {
            logger.info(" Number Of record Retreived  :"+ citizenFirSearchBeans.size());
        }

        for (CitizenFirSearchBean bean : citizenFirSearchBeans) {
            FirDetail firDetail = new FirDetail();
            firDetail.setDistrictId(citizenFirSearchBean.getDistrictId());
            firDetail.setDistrictId(citizenFirSearchBean.getDistrictId());
            firDetail.setActId(bean.getActId());
            firDetail.setActName(bean.getActName());
            firDetail.setComplainantFirstName(bean.getComplainantFirstName());
            firDetail.setComplainantMiddleName(bean.getComplainantMiddleName());
            firDetail.setComplainantLastName(bean.getComplainantLastName());
            firDetail.setComplainantName(bean.getComplainantName());
            firDetail.setDistrictCd(bean.getDistrictCd());
            firDetail.setDistrictName(bean.getDistrictName());
            firDetail.setFirBrief(bean.getFirBrief());
            firDetail.setFirFromDate(bean.getFirFromDate());
            firDetail.setFirFromDateStr(bean.getFirFromDateStr());
            firDetail.setFirNumDisplay(bean.getFirNumDisplay());
            firDetail.setFirRegNum(bean.getFirRegNum());
            regNo = bean.getFirRegNum();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            if(bean.getFirRegDate() != null){
                LocalDate firdate = LocalDate.parse(bean.getFirRegDate() , formatter);
                firDetail.setFirRegDate(firdate);
            } else {
                firDetail.setFirRegDate(null);
            }
            firDetail.setFirStatus(bean.getFirStatus());
            firDetail.setFirToDate(bean.getFirToDate());
            firDetail.setFirToDateStr(bean.getFirToDateStr());
                LocalDate date = LocalDate.parse(bean.getFirRegDate(), formatter);
                System.out.println(date.getYear());
                firDetail.setFirYear(Integer.parseInt(bean.getFirYear()) == 0 ?String.valueOf(date.getYear()) : bean.getFirYear());
            firDetail.setIsFirstSyncDone(bean.getIsFirstSyncDone());
            firDetail.setLangCd(bean.getLangCd());
            firDetail.setLinkedFirAction(bean.getLinkedFirAction());
            firDetail.setLinkingFir(bean.getLinkingFir());
            firDetail.setLinkingReason(bean.getLinkingReason());
            firDetail.setLinkingFir(bean.getLinkingFir());
            firDetail.setMisc1(bean.getMisc1());
            firDetail.setMisc2(bean.getMisc2());
            firDetail.setNameType(bean.getNameType());
            firDetail.setOrginalRecord(bean.getOrginalRecord());
            firDetail.setPageCacheRows(bean.getPageCacheRows());
            firDetail.setPageStartNo(bean.getPageStartNo());
            firDetail.setPageTotalCount(bean.getPageTotalCount());
            firDetail.setPoliceStationId(citizenFirSearchBean.getPoliceStationId());
            firDetail.setPoliceStationName(bean.getPoliceStationName());
            firDetail.setPsRecordSyncOn(bean.getPsRecordSyncOn());
            firDetail.setQueryKey(bean.getQueryKey());
            firDetail.setRecordCreatedBy(bean.getRecordCreatedBy());
            String createdDate = bean.getRecordCreatedOn().split(" ")[0];
            DateTimeFormatter createdFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            if(bean.getRecordCreatedOn() != null){
            LocalDate recordCreatedOn = LocalDate.parse(createdDate, createdFormatter);
            firDetail.setRecordCreatedOn(recordCreatedOn); }
            else {
                firDetail.setRecordCreatedOn(null);
            }
            firDetail.setRecordStatus(bean.getRecordStatus());
            firDetail.setRecordSyncFrom(bean.getRecordSyncFrom());
            firDetail.setRecordSyncOn(bean.getRecordSyncOn());
            firDetail.setRecordSyncTo(bean.getRecordSyncTo());
            firDetail.setRecordUpStringdBy(bean.getRecordUpStringdBy());
            firDetail.setRecordUpStringdFrom(bean.getRecordUpStringdFrom());
            firDetail.setRecordUpStringdOn(bean.getRecordUpStringdOn());
            firDetail.setRecordUpdatedFrom(bean.getRecordUpdatedFrom());
            firDetail.setRecordUpdatedOn(bean.getRecordUpdatedOn());
            firDetail.setRecordUpdatedby(bean.getRecordUpdatedby());
            System.out.println("===="+bean.getRegFirNo());
            firDetail.setRegFirNo(bean.getRegFirNo());
            firDetail.setReturnClassType(bean.getReturnClassType());
            firDetail.setSearchCrit(bean.isSearchCrit());
            firDetail.setSearchName(bean.getSearchName());
            firDetail.setSectionId(bean.getSectionId());
            firDetail.setSearchName(bean.getSearchName());
            firDetail.setStateCd(bean.getStateCd());
            firDetail.setStateName(bean.getStateName());
            firDetail.setStatusOfFir(bean.getStatusOfFir());
            firDetail.setUserDistrictCd(bean.getUserDistrictCd());
            firDetail.setUserPsCd(bean.getUserPsCd());
            firDetail.setUserStateCd(bean.getUserStateCd());
            logger.info(" Retreived Data is :- DistrictId : "+firDetail.getDistrictId()+
                    " PoliceStationId : "+firDetail.getPoliceStationId() +" Fir Reg Date : "+firDetail.getFirRegDate()+
                    " Fir Reg No : "+firDetail.getFirRegNum()+" Record Created on : "+firDetail.getRecordCreatedOn());

            if(regNo != null) {
                try {
                   String path =  downloadPDF(regNo);
                    System.out.println("path = " + path);
                   PDDocument document = PDDocument.load(new File(path));

                    System.out.println("=============="+document.getNumberOfPages());
                    extractTextFromScannedDocument(document,firDetail);
                    System.out.println("=================address="+firDetail.getComplainantName());
                    System.out.println("=================address="+firDetail.getAddress());
                    System.out.println("=================address="+firDetail.getPassportNo());
                    System.out.println("=================address="+firDetail.getYearOfBirth());

                    document.close();
                } catch (ConfigurationException | IOException | TesseractException e) {
                    throw new RuntimeException(e);
                }
            }
            firDetailRepository.save(firDetail);

        }
    }

    public String downloadPDF(String regNo) throws ConfigurationException {
        System.out.println("=====inside id Mapper ============");
        PropertiesConfiguration config = new PropertiesConfiguration("config.properties");
        String filePath = config.getProperty("fir.report.path").toString();
        logger.info(" File Path for Report : "+filePath);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<String> request = new HttpEntity<>(headers);

        String url ="https://cctns.delhipolice.gov.in/citizen/gefirprint.htm?firRegNo="+regNo+"&stov=46XU-O3NJ-QB17-1MHM-TCDQ-391O-571E-FYEH";
        logger.info(" download pdf URL : "+url);
        File file = null;
        try {

            file = restTemplate.execute(url, HttpMethod.GET, null, clientHttpResponse -> {
                File ret = new File(filePath + regNo + ".pdf");
                StreamUtils.copy(clientHttpResponse.getBody(), new FileOutputStream(ret));
                return ret;
            });
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            System.out.println("exceptionDBConnect = " + exceptionDBConnect);
            System.out.println("exceptionRepository = " + exceptionRepository);
            exceptionDBConnect.setExceptionCause(e.getCause() == null ? null : e.getCause().toString());
            exceptionDBConnect.setExceptionTime(LocalDateTime.now());
            exceptionDBConnect.setExceptionMessage(e.getMessage());
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            System.out.println(exceptionAsString);
            exceptionDBConnect.setExceptionStatckTrace(exceptionAsString);
            exceptionDBConnect.setFir_reg_num(regNo);
            exceptionRepository.save(exceptionDBConnect);
        }
        if(file != null){
            logger.info("File is get Saved into : "+file.getAbsolutePath());
        }
        else{
            logger.info("File is not Created check Exception ");
        }
        return  file.getAbsolutePath();
    }

    private  void extractTextFromScannedDocument(PDDocument document, FirDetail firDetail) throws IOException, TesseractException {
        // Extract images from file
        PDFRenderer pdfRenderer = new PDFRenderer(document);
//        StringBuilder out = new StringBuilder();

        ITesseract _tesseract = new Tesseract();
        System.out.println(_tesseract.toString());
        _tesseract.setDatapath("C:\\firpdf\\tessdata");
        _tesseract.setLanguage("eng+hin");

        for (int page = 0; page < document.getNumberOfPages(); page++) {
            BufferedImage bufferedImage = pdfRenderer.renderImageWithDPI(page, 300, ImageType.RGB);

            // Create a temp image file
            File tempFile = File.createTempFile("tempfile_" + page, ".png");
            //System.out.println(tempFile.getAbsolutePath());
            ImageIO.write(bufferedImage, "png", tempFile);

            String result = _tesseract.doOCR(tempFile);
            if(page==0) {
                System.out.println("Page==0");
                String[] lines = result.split("\\n");
//                myWriter.write("---------------------------------------------------");
//                myWriter.write("File Name="+name+"\n");
                int count=0;
                for(String findname:lines) {

                    if(findname.trim().startsWith("(a)Name"))
                    {
                        String cname[] = findname.split(" ");
                        String s = "";
                        for(int i=1;i< cname.length;i++){
                            s += cname[i];
                        }
                        firDetail.setComplainantName(s);
                    }
                    if(findname.trim().startsWith("(b)Date/Year of Birth"))
                    {
                        String arg[] = findname.split(" ");
                        if(!arg[6].equals("Nationality")){
                            firDetail.setYearOfBirth(arg[6]);
                        }
                    }
                    if(findname.trim().startsWith("(c)Passport"))
                    {
                        firDetail.setPassportNo(findname);
                    }
                    if(findname.trim().startsWith("(e)Address"))
                    {
                        String caddress[] = findname.split(" ");
                        String s = "";
                        for(int i=1;i< caddress.length;i++){
                            s += caddress[i];
                        }
                       firDetail.setAddress(s);
                    }
                }
                break;
            }

        }

    }

}
