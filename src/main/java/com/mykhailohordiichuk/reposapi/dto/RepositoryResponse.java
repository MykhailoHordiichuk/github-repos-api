package com.mykhailohordiichuk.reposapi.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class RepositoryResponse {
    private String repositoryName;
    private String ownerLogin;
    private List<BranchResponse> branches;
}
