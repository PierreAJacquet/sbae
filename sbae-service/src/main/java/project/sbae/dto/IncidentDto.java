package project.sbae.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class IncidentDto {

    private Integer id;

    private String title;

    private String description;

    private String severity;

    private PersonDto person;

    private LocalDateTime createdAt;
}
