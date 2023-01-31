package com.prgrms.bdbks.domain.store.dto;

import org.locationtech.jts.geom.Point;

import com.prgrms.bdbks.domain.store.entity.Store;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class StoreResponse {
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Information {

		private String id;

		private String name;

		private String lotNumberAddress;

		private String roadNameAddress;

		private PointInformation point;

		public Information(Store store) {
			this.id = store.getId();
			this.name = store.getName();
			this.lotNumberAddress = store.getLotNumberAddress();
			this.roadNameAddress = store.getRoadNameAddress();
			this.point = new PointInformation(store.getPosition());
		}
	}

	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	private static class PointInformation {

		private double latitude;

		private double longitude;

		public PointInformation(Point point) {
			this.longitude = point.getY();
			this.latitude = point.getY();
		}
	}
}
