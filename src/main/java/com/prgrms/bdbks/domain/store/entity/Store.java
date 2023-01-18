package com.prgrms.bdbks.domain.store.entity;

import static com.google.common.base.Preconditions.*;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.util.StringUtils;

import com.prgrms.bdbks.common.domain.AbstractTimeColumn;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "stores")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Store extends AbstractTimeColumn {

	@Id
	@Column(name = "stores_id")
	private String id;

	@NotNull
	@Column(name = "store_id", length = 50)
	private String name;

	@NotNull
	@Column(name = "lot_number_address", length = 100)
	private String lotNumberAddress;

	@NotNull
	@Column(name = "road_name_address", length = 100)
	private String roadNameAddress;

	@NotNull
	@Column(name = "latitude", columnDefinition = "decimal")
	private BigDecimal latitude;

	@NotNull
	@Column(name = "longitude", columnDefinition = "decimal")
	private BigDecimal longitude;

	@Builder
	protected Store(String id, String name, String lotNumberAddress,
		String roadNameAddress, BigDecimal latitude, BigDecimal longitude) {

		checkArgument(StringUtils.hasText(id) && !id.isBlank(), "id는 null 이거나 공백일 수 없습니다.");
		validationName(name);
		validationAddress(lotNumberAddress);
		validationAddress(roadNameAddress);
		checkNotNull(latitude, "latitude는 null 일 수 없습니다.");
		checkNotNull(longitude, "longitude는 null 일 수 없습니다.");

		this.id = id;
		this.name = name;
		this.lotNumberAddress = lotNumberAddress;
		this.roadNameAddress = roadNameAddress;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	private void validationAddress(String address) {
		checkArgument(StringUtils.hasText(address) && !address.isBlank(), "address의 길이는 0 이상이여야 합니다.");
		checkArgument(address.length() <= 100, "address는 100자를 넘을 수 없습니다.");
	}

	private void validationName(String name) {
		checkArgument(StringUtils.hasText(name) && !name.isBlank(), "storeName의 길이는 0 이상이여야 합니다.");
		checkArgument(name.length() <= 50, "storeName은 50자를 넘을 수 없습니다.");
	}
}
