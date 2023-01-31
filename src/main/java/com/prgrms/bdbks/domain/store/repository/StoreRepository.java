package com.prgrms.bdbks.domain.store.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.prgrms.bdbks.domain.store.entity.Store;
import com.prgrms.bdbks.global.util.Location;

@Repository
public interface StoreRepository extends JpaRepository<Store, String> {

	@Query("SELECT s FROM Store s WHERE s.lotNumberAddress LIKE %:district%")
	List<Store> findTop10StoresByLotNumberAddress(@Param("district") String district);

	@Query(nativeQuery = true, value =
		"SELECT *, ST_DISTANCE_SPHERE(ST_POINTFROMTEXT(:#{#location.toPointText()}, 4326), ST_SRID(stores.position, 4326)) dist FROM stores "
			+ "WHERE ST_DISTANCE_SPHERE(ST_POINTFROMTEXT(:#{#location.toPointText()}, 4326), ST_SRID(stores.position, 4326)) < :distance "
			+ "ORDER BY dist ASC")
	List<Store> findAllByDistance(@Param("location") Location location, @Param("distance") int distance);

	@Query(nativeQuery = true,
		value =
			"select ST_DISTANCE_SPHERE(ST_POINTFROMTEXT(:#{#location.toPointText()}, 4326), ST_SRID(stores.position, 4326)) dist "
				+ "FROM stores ")
	List<Integer> findDistance(@Param("location") Location location);

	@Query(nativeQuery = true, value = "SELECT * FROM stores LIMIT 1")
		//TODO : UserAuthority 반영
	Optional<Store> findStoreByUserId(long id);

	@Query(nativeQuery = true, value = "SELECT * FROM stores LIMIT 1")
		//TODO : UserAuthority 반영
	Optional<Store> findStoreByLoginId(String longId);
}
