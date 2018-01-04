package com.pricecompare.common.data.reopsitories;

import com.pricecompare.common.data.entities.AgentLoadMore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AgentLoadMoreRepository extends JpaRepository<AgentLoadMore, Integer>
{
    @Query(value = "select a.* from agent_loadmore_methods a where a.agent_id = :agentId", nativeQuery = true)
    List<AgentLoadMore> findByAgentId(@Param("agentId") int agentId);
}
