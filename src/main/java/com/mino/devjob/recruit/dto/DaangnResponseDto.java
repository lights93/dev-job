package com.mino.devjob.recruit.dto;

import java.util.List;
import java.util.Map;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class DaangnResponseDto {
	private RecordMap recordMap;

	@Value
	@Builder
	public static class RecordMap {
		private String block;

//		@Value
//		public static class Block {
//			private Map<String, Item> itemMapList;
//
//			public static class Item {
//				private String parent_id;
//				private String type;
//				private int created_time;
//				private String properties;
//
//			}
//		}
	}
}
