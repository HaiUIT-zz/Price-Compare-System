package com.pricecompare.common.data.pojos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AgentDTO
{
    private int id;
    private String code;
    private String name;
    private String searchUrl;
    protected boolean isDeleted;
    private boolean acceptable;

    public AgentDTO(int id, String code, String name, String search_url, boolean is_deleted)
    {
        this.id = id;
        this.code = code;
        this.name = name;
        this.searchUrl = search_url;
        this.isDeleted = is_deleted;
        acceptable = search_url != null && !search_url.isEmpty();
    }
}
