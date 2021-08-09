package com.remmy.backend.pojo;

import lombok.*;

/**
 * @author yejiaxin
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ZkClusterConfig {

    private Integer id;

    private String name;

    private String description;

    private String url;

    private String acl;
}
