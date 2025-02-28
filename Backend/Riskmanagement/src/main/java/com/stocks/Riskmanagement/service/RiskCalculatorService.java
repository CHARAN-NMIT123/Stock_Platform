package com.stocks.Riskmanagement.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stocks.Riskmanagement.dto.OrderDTO;
import com.stocks.Riskmanagement.dto.RiskCalculatorDTO;
import com.stocks.Riskmanagement.entites.RiskCalculator;
import com.stocks.Riskmanagement.repository.RiskCalculatorRepository;

@Service
public class RiskCalculatorService {
	@Autowired
	private RiskCalculatorRepository riskCalculatorRepository;

	@Autowired
	private ModelMapper modelmapper;

//	public List<RiskCalculator> getAllRisks() {
//		return riskCalculatorRepository.findAll();
//	}

	public RiskCalculatorDTO calculatePositionSizing(RiskCalculatorDTO riskCalculatorDTO) {
		RiskCalculator riskCalculator = processDTOToEntity(riskCalculatorDTO);
		int riskPoint = (int) (riskCalculator.getEntryPrice() - riskCalculator.getStopLoss());
		System.out.println("risktrade"+riskCalculator.getRiskPerTrade());
		System.out.println("riskpoint"+riskPoint);
		float numOfShares = riskCalculator.getRiskPerTrade() / riskPoint;
//		System.out.println("numofSharesold"+numOfShares);
		if(numOfShares==0) {
			numOfShares = 1;
		}
//		System.out.println("numofSharenew"+numOfShares);
		riskCalculator.setNumOfShares(numOfShares);
		riskCalculator.setRiskPoint(riskPoint);

		riskCalculatorRepository.save(riskCalculator);
		RiskCalculatorDTO updatedRiskCalculatorDTO = modelmapper.map(riskCalculator, RiskCalculatorDTO.class);
		updatedRiskCalculatorDTO.setRiskPoint(riskPoint);
		updatedRiskCalculatorDTO.setNumOfShares((int) numOfShares);

		return updatedRiskCalculatorDTO;
	}

//	public RiskCalculator handleMarketPlan(RiskCalculator riskCalculator, double marketPrice) {
//		riskCalculator.setEntryPrice(marketPrice);
//		return riskCalculatorRepository.save(riskCalculator);
//	}

	public RiskCalculatorDTO handleStopLoss(RiskCalculatorDTO riskCalculatorDTO, double currentPrice) {
		if (currentPrice < riskCalculatorDTO.getStopLoss()) {
			throw new IllegalArgumentException("Sell price cannot be less stoploss");
		}
		else {
		RiskCalculator riskCalculator = modelmapper.map(riskCalculatorDTO, RiskCalculator.class);
		riskCalculator.setNumOfShares(0);
		riskCalculatorRepository.save(riskCalculator);
		RiskCalculatorDTO updatedRiskCalculatorDTO = modelmapper.map(riskCalculator, RiskCalculatorDTO.class);
		return updatedRiskCalculatorDTO;
		}

	}

	public RiskCalculatorDTO processOrderToRisk(OrderDTO orderDTO) {
		RiskCalculatorDTO riskCalculatorDTO = new RiskCalculatorDTO();
		riskCalculatorDTO.setOrderID((orderDTO.getOrderId()));
		riskCalculatorDTO.setEntryPrice(orderDTO.getEntryPrice());
		riskCalculatorDTO.setStopLoss(orderDTO.getStopLoss());
		riskCalculatorDTO.setRiskPerTrade(orderDTO.getRiskPerTrade());
		riskCalculatorDTO.setTransType(orderDTO.getTransType());
		riskCalculatorDTO.setTypeOfSell(orderDTO.getTypeOfSell());
		riskCalculatorDTO.setNumOfShares(orderDTO.getNumShares());
		riskCalculatorDTO.setTypeOfPurchase(orderDTO.getTypeOfPurchase());
		riskCalculatorDTO.setAccBalance(orderDTO.getBalance());
		
		return riskCalculatorDTO;
	}
	public RiskCalculator processDTOToEntity(RiskCalculatorDTO riskCalculatorDTO) {
		RiskCalculator riskCalculator = new RiskCalculator();
		riskCalculator.setEntryPrice(riskCalculatorDTO.getEntryPrice());
		riskCalculator.setStopLoss(riskCalculatorDTO.getStopLoss());
		riskCalculator.setRiskPerTrade(riskCalculatorDTO.getRiskPerTrade());
		riskCalculator.setTransType(riskCalculatorDTO.getTransType());
		riskCalculator.setTypeOfPurchase(riskCalculatorDTO.getTypeOfPurchase());
		riskCalculator.setTypeOfSell(riskCalculatorDTO.getTypeOfSell());
		riskCalculator.setOrderID(riskCalculatorDTO.getOrderID());
		riskCalculator.setAccBalance(riskCalculatorDTO.getAccBalance());
		return riskCalculator;
	}

}