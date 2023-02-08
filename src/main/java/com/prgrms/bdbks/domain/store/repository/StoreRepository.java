package com.prgrms.bdbks.domain.store.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.prgrms.bdbks.common.util.Location;
import com.prgrms.bdbks.domain.store.entity.Store;

@Repository
public interface StoreRepository extends JpaRepository<Store, String> {

	@Query("SELECT s FROM Store s WHERE s.lotNumberAddress LIKE %:district%")
	List<Store> findTop10StoresByLotNumberAddress(@Param("district") String district);

	// , ST_DISTANCE_SPHERE(ST_POINTFROMTEXT(:#{#location.toPointText()}, 4326), ST_SRID(stores.position, 4326)) dist
	@Query(nativeQuery = true, value =
		"SELECT stores_id, created_at, updated_at, lot_number_address, store_name, st_astext(position) as position, road_name_address FROM stores "
			+ "WHERE ST_DISTANCE_SPHERE(ST_POINTFROMTEXT(:#{#location.toPointText()}, 4326), ST_SRID(stores.position, 4326)) < :distance "
			+ "ORDER BY ST_DISTANCE_SPHERE(ST_POINTFROMTEXT(:#{#location.toPointText()}, 4326), ST_SRID(stores.position, 4326)) ASC limit 10")
	List<Store> findAllByDistance(@Param("location") Location location, @Param("distance") int distance);

	@Query(nativeQuery = true,
		value =
			"select ST_DISTANCE_SPHERE(ST_POINTFROMTEXT(:#{#location.toPointText()}, 4326), ST_SRID(stores.position, 4326)) dist "
				+ "FROM stores ")
	List<Integer> findDistance(@Param("location") Location location);

	@Query(nativeQuery = true, value = "SELECT "
		+ "s.stores_id, s.created_at, s.updated_at, lot_number_address, store_name, st_astext(position) as position, road_name_address from stores s "
		+ "inner join user_authorities ua on s.stores_id = ua.stores_id "
		+ "where ua.user_id = :id   LIMIT 1")
		//TODO : UserAuthority 반영
	Optional<Store> findStoreByUserId(@Param("id") long id);

	@Query(nativeQuery = true, value = "SELECT "
		+ "stores_id, created_at, updated_at, lot_number_address, store_name, st_astext(position) as position, road_name_address FROM stores LIMIT 1")
		//TODO : UserAuthority 반영
	Optional<Store> findStoreByLoginId(@Param("longId") String longId);
}
