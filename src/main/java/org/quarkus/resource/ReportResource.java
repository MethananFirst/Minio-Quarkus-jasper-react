package org.quarkus.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import net.sf.jasperreports.engine.JRException;
import org.quarkus.dto.ReportDto;
import org.quarkus.service.ReportService;

@ApplicationScoped
@Path("/report")
public class ReportResource {
    private final ReportService reportService;

    public ReportResource(ReportService reportService) {
        this.reportService = reportService;
    }

    @POST
    @Path("/generate/{bucketName}/{fileName}/{jasper}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("application/pdf")
    public Response generateReport(JsonObject jsonObject,
                                   @PathParam("bucketName") String bucketName,
                                   @PathParam("fileName") String originalFileName,
                                   @PathParam("jasper") String jasper) throws JRException, JsonProcessingException {
        String fileName = originalFileName + ".pdf";

        return Response
                .ok(reportService.generatePdfReport(jsonObject, bucketName, fileName, jasper), "application/pdf")
                .header("Content-Disposition", "inline; filename=\"Report.pdf\"")
                .build();
    }
}
