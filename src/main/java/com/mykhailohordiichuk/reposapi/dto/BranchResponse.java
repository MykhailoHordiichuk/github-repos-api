package com.mykhailohordiichuk.reposapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BranchResponse {
    private String name;
    private String lastCommitSha;
}
