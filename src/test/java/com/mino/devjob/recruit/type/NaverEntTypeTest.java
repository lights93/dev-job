package com.mino.devjob.recruit.type;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class NaverEntTypeTest {

	@Test
	void getByCode() {
		assertEquals(NaverEntType.NEW, NaverEntType.getByCode("001"));

		assertThrows(IllegalArgumentException.class, () -> NaverEntType.getByCode("fail"));
	}
}