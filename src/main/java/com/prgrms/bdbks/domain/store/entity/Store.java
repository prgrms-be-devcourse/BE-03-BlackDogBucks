package com.prgrms.bdbks.domain.store.entity;

import static com.google.common.base.Preconditions.*;

import javax.persistence.AttributeConverter;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.ColumnTransformer;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.prgrms.bdbks.common.domain.AbstractTimeColumn;
import com.prgrms.bdbks.common.exception.PointParseException;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "stores")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Store extends AbstractTimeColumn {

	@Id
	@Column(name = "stores_id", length = 10)
	private String id;

	@NotNull
	@Column(name = "store_name", length = 50, nullable = false)
	private String name;

	@NotNull
	@Column(name = "lot_number_address", length = 100, nullable = false)
	private String lotNumberAddress;

	@NotNull
	@Column(name = "road_name_address", length = 100, nullable = false)
	private String roadNameAddress;

	@Convert(converter = PointConverter.class)
	@ColumnTransformer(write = "ST_PointFromText(?, 4326)", read = "ST_AsText(position)")
	@NotNull
	@Column(name = "position", columnDefinition = "POINT SRID 4326", nullable = false)
	private Point position;

	@Builder
	protected Store(String id, String name, String lotNumberAddress,
		String roadNameAddress, Point position) {

		validationId(id);
		validationName(name);
		validationAddress(lotNumberAddress);
		validationAddress(roadNameAddress);
		checkNotNull(position, "position은 null 일 수 없습니다.");

		this.id = id;
		this.name = name;
		this.lotNumberAddress = lotNumberAddress;
		this.roadNameAddress = roadNameAddress;
		this.position = position;
	}

	private void validationId(String id) {
		checkArgument(StringUtils.hasText(id), "id는 null 이거나 공백일 수 없습니다.");
		checkArgument(id.length() <= 10, "id는 10자를 넘을 수 없습니다.");
	}

	private void validationAddress(String address) {
		checkArgument(StringUtils.hasText(address), "address의 길이는 0 이상이여야 합니다.");
		checkArgument(address.length() <= 100, "address는 100자를 넘을 수 없습니다.");
	}

	private void validationName(String name) {
		checkArgument(StringUtils.hasText(name), "storeName의 길이는 0 이상이여야 합니다.");
		checkArgument(name.length() <= 50, "storeName은 50자를 넘을 수 없습니다.");
	}

	@Component
	public static class PointConverter implements AttributeConverter<Point, String> {
		static WKTReader wktReader = new WKTReader();

		@Override
		public String convertToDatabaseColumn(Point attribute) {
			return attribute.toText();
		}

		@Override
		public Point convertToEntityAttribute(String dbData) {
			try {
				// String decoded = new String(dbData.getBytes(), StandardCharsets.UTF_8);
				System.out.println("dbdata : " + dbData);
				return (Point)wktReader.read(dbData);
			} catch (ParseException e) {
				throw new PointParseException(String.format("위경도 파싱에 실패했습니다. %s", dbData));
			}
		}
	}
}
