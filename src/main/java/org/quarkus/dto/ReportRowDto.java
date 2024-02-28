package org.quarkus.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ReportRowDto {
    @JsonProperty("name")
    @NotEmpty(message = "can not be null")
    @Size(min = 1, message = "can not be blank")
    private String name;

    @JsonProperty("age")
    private String age ;

    @JsonProperty("gender")
    private String gender ;

    @JsonProperty("phone")
    private String phone ;

    @JsonProperty("birthday")
    private String birthday;

    // Getters and setters

    public String getName() {
        return name;
    }

    public String getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public String getPhone() {
        return phone;
    }

    public String getBirthday() {
        return birthday;
    }
}
