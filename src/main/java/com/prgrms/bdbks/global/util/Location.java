package com.prgrms.bdbks.global.util;

import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

import com.prgrms.bdbks.common.exception.PointParseException;

public class Location {

	private final double latitude;

	private final double longitude;

	private final Point point;

	public Location(double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;

		try {
			this.point = (Point)new WKTReader().read(String.format("POINT(%f %f)", latitude, longitude));
		} catch (ParseException e) {
			throw new PointParseException(String.format("위경도 파싱에 실패했습니다. %s,%s", latitude, longitude));
		}
	}

	public String toPointText() {
		return point.toText();
	}

}

