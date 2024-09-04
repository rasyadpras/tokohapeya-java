package com.enigma.challenge.tokohapeya.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PagingResponse {
    private int totalPage;
    private int page;
    private int size;
    private Boolean hasNext;
    private Boolean hasPrev;
}
