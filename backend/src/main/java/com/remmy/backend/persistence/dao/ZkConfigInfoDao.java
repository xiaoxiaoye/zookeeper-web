package com.remmy.backend.persistence.dao;

import com.remmy.backend.persistence.entity.ZkConfigInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author yejiaxin
 */
@Repository
public interface ZkConfigInfoDao extends JpaRepository<ZkConfigInfo, Integer> {
}
