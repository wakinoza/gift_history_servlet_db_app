package com.gift.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.gift.app.entity.GiftItem;

/**
 * GiftItemsのリポジトリ
 */
@Repository
public interface GiftItemRepository extends JpaRepository<GiftItem, Integer> {

}
