package com.stocks.Riskmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.stocks.Riskmanagement.entites.RiskCalculator;

public interface RiskCalculatorRepository extends JpaRepository<RiskCalculator,String>
{

}
