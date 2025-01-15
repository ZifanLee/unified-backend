package com.zifan.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;
import java.util.List;

@Document(collection = "groups")
@Data
public class Group {

    @Id
    private String id;
    private String name;
    private String creatorId;
    private List<String> members;
    private String type;
    private Date createdAt;
    private Date updatedAt;

}