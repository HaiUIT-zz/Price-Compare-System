package com.pricecompare.repositories;

import com.pricecompare.entities.Agent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgentRepository extends JpaRepository<Agent, Long>
{
}
