package com.customdb.keyword;

import lombok.Builder;
import lombok.Getter;

/**
 * @author mingbozhang
 */
@Builder
public class Limit {

    @Getter
    private long size;
}
