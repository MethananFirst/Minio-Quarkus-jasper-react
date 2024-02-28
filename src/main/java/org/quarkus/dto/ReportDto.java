package org.quarkus.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReportDto {
    @JsonProperty("companyName")
    @NotEmpty(message = "Company name cannot be null")
    @Size(min = 1, max = 30, message = "Company name should be between 1 and 30 characters")
    private String companyName;

    @JsonProperty("companyUrl")
    @NotEmpty(message = "Company URL cannot be null")
    @Size(min = 1, max = 30, message = "Company URL should be between 1 and 30 characters")
    private String companyUrl;

    @Valid
    @JsonProperty("rows")
    @NotEmpty(message = "Rows cannot be null")
    @Size(min = 1, message = "Rows cannot be empty")
    private List<ReportRowDto> rows;

    // Add getters and setters

    public String getCompanyName() {
        return companyName;
    }

    public String getCompanyUrl() {
        return companyUrl;
    }

    public List<ReportRowDto> getRows() {
        return rows;
    }

    // Add setters

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setCompanyUrl(String companyUrl) {
        this.companyUrl = companyUrl;
    }

    public void setRows(List<ReportRowDto> rows) {
        this.rows = rows;
    }
}
