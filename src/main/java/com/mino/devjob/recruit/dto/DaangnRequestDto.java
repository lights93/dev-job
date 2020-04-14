package com.mino.devjob.recruit.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class DaangnRequestDto {
	private String pageId;
	private int limit;
	private int chunkNumber;
	private boolean verticalColumns;
}
