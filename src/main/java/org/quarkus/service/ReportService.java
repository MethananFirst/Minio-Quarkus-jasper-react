package org.quarkus.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.json.JsonObject;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.quarkus.dto.LetterDto;
import org.quarkus.dto.ReportDto;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class ReportService {
    private JasperReport compileReport;

    private String jasperFileName;
    @Inject
    private MinioService minioUploader;
    private JasperReport compiledReport;

    @PostConstruct
    public void init() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("reports.config/ExampleReport.jrxml")) {
            if (inputStream != null) {
                compileReport = JasperCompileManager.compileReport(inputStream);
            }
        } catch (IOException | JRException e) {
            e.printStackTrace();
        }
    }

    public byte[] generatePdfReport(JsonObject jsonObject, String bucketName, String fileName, String template) throws JsonProcessingException, JRException {
        String resourcePath = "reports.config/";
        if (template.equals("1")) {
            jasperFileName = "ExampleReport.jrxml";

            ObjectMapper objectMapper = new ObjectMapper();
            ReportDto dto = objectMapper.readValue(jsonObject.toString(), ReportDto.class);

            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourcePath + jasperFileName);
            if (inputStream != null) {
                compiledReport = JasperCompileManager.compileReport(inputStream);
            } else {
                throw new IllegalStateException("Resource not found: reports.config/" + jasperFileName);
            }

            Map<String, Object> params = new HashMap<>();
            params.put("companyUrl", dto.getCompanyUrl());
            params.put("companyName", dto.getCompanyName());

            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(dto.getRows());

            try {
                JasperPrint jasperPrint = JasperFillManager.fillReport(compileReport, params, dataSource);
                byte[] pdfBytes = JasperExportManager.exportReportToPdf(jasperPrint);

                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(pdfBytes);

                // Upload the PDF to MinIO
                minioUploader.uploadFile(bucketName, byteArrayInputStream, fileName);

                return pdfBytes;
            } catch (JRException e) {
                e.printStackTrace();
                return new byte[0];
            }
        } else if(template.equals("2")){
            jasperFileName = "Letter.jrxml";

            try {

                InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourcePath + jasperFileName);
                if (inputStream == null) {
                    throw new IllegalStateException("Jasper file not found: " + jasperFileName);
                }

                compiledReport = JasperCompileManager.compileReport(inputStream);

//                LetterDto letterDto = new LetterDto();
                ObjectMapper objectMapper = new ObjectMapper();
                LetterDto letterDto = objectMapper.readValue(jsonObject.toString(), LetterDto.class);

                Map<String, Object> parameters = new HashMap<>();
                parameters.put("senderAddress", letterDto.getSenderAddress());
                parameters.put("date", letterDto.getDate());
                parameters.put("recipientName", letterDto.getRecipientName());
                parameters.put("recipientAddress", letterDto.getRecipientAddress());
                parameters.put("salutation", letterDto.getSalutation());
                parameters.put("content1", letterDto.getContent1());
                parameters.put("content2", letterDto.getContent2());
                parameters.put("content3", letterDto.getContent3());
                parameters.put("closing", letterDto.getClosing());
                parameters.put("signature", letterDto.getSignature());

                JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(Collections.singleton(letterDto));
                JasperPrint jasperPrint = JasperFillManager.fillReport(compiledReport, parameters, dataSource);
                byte[] pdfData = JasperExportManager.exportReportToPdf(jasperPrint);

                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(pdfData);

                minioUploader.uploadFile(bucketName, byteArrayInputStream, fileName);

                return pdfData;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        return null;
    }

}



