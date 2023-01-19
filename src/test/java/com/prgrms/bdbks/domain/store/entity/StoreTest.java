package com.prgrms.bdbks.domain.store.entity;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Coordinate;

@DisplayName("store 테스트")
class StoreTest  {

	private final String id = "34534523";

	private final String name = "가로수길점";

	private final String lotNumberAddress = "서울시 강남구 신사동 533-11";

	private final String roadNameAddress = "서울특별시 강남구 논현로175길 94";

	private final GeometryFactory gf = new GeometryFactory();

	private Point position = gf.createPoint(new Coordinate(37.5231593,127.02162499999997));


	@DisplayName("생성 - Store() - Store의 모든 필드가 유효한 경우 생성에 성공한다.")
	@Test
	void constructor_create_success()  {
		// Given
		// when & then
		assertDoesNotThrow(() -> {
			Store.builder()
				.id(id)
				.name(name)
				.lotNumberAddress(lotNumberAddress)
				.roadNameAddress(roadNameAddress)
				.position(position)
				.build();
		});
	}

	@Test
	@DisplayName("생성- Store() - id가 10자를 넘을 경우 생성에 실패한다.")
	void constructor_create_over_length_id_fail() {
		// Given
		// when & then
		assertThrows(IllegalArgumentException.class,
			() -> Store.builder()
				.id("12345678910")
				.name(name)
				.lotNumberAddress(lotNumberAddress)
				.roadNameAddress(roadNameAddress)
				.position(position)
				.build()
		);
	}


	@ParameterizedTest
	@NullSource
	@ValueSource(strings = {"", "  ", "  \n"})
	@DisplayName("생성- Store() - id가 null 혹은 공백일 경우 생성에 실패한다.")
	void constructor_create_null_or_black_id_fail(String invalidId)  {
		// Given
		// when & then
		assertThrows(IllegalArgumentException.class,
			() -> Store.builder()
				.id(invalidId)
				.name(name)
				.lotNumberAddress(lotNumberAddress)
				.roadNameAddress(roadNameAddress)
				.position(position)
				.build()
		);
	}

	@ParameterizedTest
	@NullSource
	@ValueSource(strings = {"", "  ", "  \n"})
	@DisplayName("생성- Store() - name이 null 혹은 공백일 경우 생성에 실패한다.")
	void constructor_create_null_or_black_name_fail(String invalidName) {
		// Given
		// when & then
		assertThrows(IllegalArgumentException.class,
			() -> Store.builder()
				.id(id)
				.name(invalidName)
				.lotNumberAddress(lotNumberAddress)
				.roadNameAddress(roadNameAddress)
				.position(position)
				.build()
		);
	}

	@Test
	@DisplayName("생성- Store() - name이 50자 이상일 경우 생성에 실패한다.")
	void constructor_create_over_length_name_fail()  {
		// Given
		// when & then
		assertThrows(IllegalArgumentException.class,
			() -> Store.builder()
				.id(id)
				.name("123456789012345678901234567890123456789012345678901")
				.lotNumberAddress(lotNumberAddress)
				.roadNameAddress(roadNameAddress)
				.position(position)
				.build()
		);
	}

	@ParameterizedTest
	@NullSource
	@ValueSource(strings = {"", "  ", "  \n"})
	@DisplayName("생성- Store() - address가 null 혹은 공백일 경우 생성에 실패한다.")
	void constructor_create_null_or_black_address_fail(String invalidAddress)  {
		// Given
		// when & then
		assertThrows(IllegalArgumentException.class,
			() -> Store.builder()
				.id(id)
				.name(name)
				.lotNumberAddress(invalidAddress)
				.roadNameAddress(invalidAddress)
				.position(position)
				.build()
		);
	}

	@Test
	@DisplayName("생성- Store() - address가 100자 이상일 경우 생성에 실패한다.")
	void constructor_create_over_length_address_fail()  {
		// Given
		// when & then
		String hundredLengthAddress = "12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901";
		assertThrows(IllegalArgumentException.class,
			() -> Store.builder()
				.id(id)
				.name(hundredLengthAddress)
				.lotNumberAddress(hundredLengthAddress)
				.roadNameAddress(roadNameAddress)
				.position(position)
				.build()
		);
	}

	@Test
	@DisplayName("생성- Store() - position이 null 일 경우 생성에 실패한다.")
	void constructor_create_null_position_fail() {
		// Given
		// when & then
		assertThrows(NullPointerException.class,
			() -> Store.builder()
				.id(id)
				.name(name)
				.lotNumberAddress(lotNumberAddress)
				.roadNameAddress(roadNameAddress)
				.build()
		);
	}

}