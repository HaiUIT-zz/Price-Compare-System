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
    private boolean acceptable;

    public AgentDTO(int id, String code, String name, String search_url)
    {
        this.id = id;
        this.code = code;
        this.name = name;
        this.searchUrl = search_url;
        acceptable = search_url != null && !search_url.isEmpty();
    }
}
