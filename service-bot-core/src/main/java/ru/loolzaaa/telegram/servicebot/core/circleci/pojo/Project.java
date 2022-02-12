package ru.loolzaaa.telegram.servicebot.core.circleci.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Project {
    private String id;
    private String name;
    private String slug;
}
