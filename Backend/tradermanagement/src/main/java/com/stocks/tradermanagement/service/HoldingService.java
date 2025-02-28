package com.stocks.tradermanagement.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stocks.tradermanagement.dtos.HoldingDTO;
import com.stocks.tradermanagement.dtos.OrderDTO;
import com.stocks.tradermanagement.entities.Holdings;
import com.stocks.tradermanagement.exceptions.StockAlreadySoldException;
import com.stocks.tradermanagement.repository.HoldingsRepo;

@Service
public class HoldingService {
    @Autowired
    private HoldingsRepo holdingsRepo;

    @Autowired
    private ModelMapper modelMapper;

    // this is taking an orderDTO and converting it to a holdingDTO
    public HoldingDTO convertOrderToHolding(OrderDTO orderDTO) {
        HoldingDTO holdingDTO = new HoldingDTO();
        holdingDTO.setAccountId(orderDTO.getAccountId());
        holdingDTO.setStockId(orderDTO.getStockId());
        holdingDTO.setBoughtAt(orderDTO.getEntryPrice());
        holdingDTO.setSoldAt(orderDTO.getEntryPrice());
        holdingDTO.setNumShares(orderDTO.getNumShares());

        return holdingDTO;
    }

    // Endpoint - 3
    public int processMarketBuyOrder(OrderDTO orderDTO) {
        HoldingDTO holdingDTO = convertOrderToHolding(orderDTO);
        holdingDTO.setSoldAt(0);
        // It Check for Existing Holding and Update Existing Holding
        if (holdingsRepo.findByAccountIdAndStockId(holdingDTO.getAccountId(), holdingDTO.getStockId()) != null) {
            holdingDTO.setHoldingId( //Sets the holdingId of the holdingDTO to the ID of the existing holding.
                    holdingsRepo.findByAccountIdAndStockId(holdingDTO.getAccountId(), holdingDTO.getStockId()));
                    Holdings existingHolding = holdingsRepo.findById(holdingDTO.getHoldingId()).orElseThrow();
                    holdingDTO.setNumShares(existingHolding.getNumShares() + holdingDTO.getNumShares());

        }
        //creating a new holding object from the holdingDTO
        Holdings holding = modelMapper.map(holdingDTO, Holdings.class);
        if(holding.getNumShares()>0)
        {
                holding.setStatus("bought");
        }
        holdingsRepo.save(holding);
        HoldingDTO updateHoldingDTO = modelMapper.map(holding, HoldingDTO.class);
        return updateHoldingDTO.getNumShares();
    }
    // Endpoint - 2 
    public int processMarketSellOrder(OrderDTO orderDTO) {
        HoldingDTO holdingDTO = convertOrderToHolding(orderDTO);
        holdingDTO.setBoughtAt(0);
        if (holdingsRepo.findByAccountIdAndStockId(holdingDTO.getAccountId(), holdingDTO.getStockId()) != null) {
            holdingDTO.setHoldingId(
                    holdingsRepo.findByAccountIdAndStockId(holdingDTO.getAccountId(), holdingDTO.getStockId()));
                    Holdings existingHolding = holdingsRepo.findById(holdingDTO.getHoldingId()).orElseThrow();
                     if(existingHolding.getStatus().equalsIgnoreCase("sold")){
                         throw new StockAlreadySoldException("Stock already sold");
                     }
                    holdingDTO.setNumShares(existingHolding.getNumShares() - holdingDTO.getNumShares());
        }
        Holdings holding = modelMapper.map(holdingDTO, Holdings.class);
        if(holding.getNumShares()>0)
        {
                holding.setStatus("Holding");
        }
        else{
            holding.setStatus("sold");
        }
        holdingsRepo.save(holding);
        HoldingDTO updateHoldingDTO = modelMapper.map(holding, HoldingDTO.class);
        return updateHoldingDTO.getNumShares();
    }

    public int processPostionSizingMarketOrder(OrderDTO orderDTO) {
        HoldingDTO holdingDTO = convertOrderToHolding(orderDTO);
        if (holdingsRepo.findByAccountIdAndStockId(holdingDTO.getAccountId(), holdingDTO.getStockId()) != null) {
            holdingDTO.setHoldingId(
                    holdingsRepo.findByAccountIdAndStockId(holdingDTO.getAccountId(), holdingDTO.getStockId()));
                    Holdings existingHolding = holdingsRepo.findById(holdingDTO.getHoldingId()).orElseThrow();
                    holdingDTO.setNumShares(existingHolding.getNumShares() + holdingDTO.getNumShares());
        }
        Holdings holding = modelMapper.map(holdingDTO, Holdings.class);
        holding.setStatus("bought");
        holdingsRepo.save(holding);
        HoldingDTO updateHoldingDTO = modelMapper.map(holding, HoldingDTO.class);
        return updateHoldingDTO.getNumShares();
    }

    public int processStopLossSellOrder(OrderDTO orderDTO) {
        HoldingDTO holdingDTO = convertOrderToHolding(orderDTO);
        holdingDTO.setStatus("Sold");
        holdingDTO.setNumShares(0);
        if (holdingsRepo.findByAccountIdAndStockId(holdingDTO.getAccountId(), holdingDTO.getStockId()) != null) {
            holdingDTO.setHoldingId(
                    holdingsRepo.findByAccountIdAndStockId(holdingDTO.getAccountId(), holdingDTO.getStockId()));
                    Holdings existingHolding = holdingsRepo.findById(holdingDTO.getHoldingId()).orElseThrow();
                    if(existingHolding.getStatus().equalsIgnoreCase("sold")){
                        throw new StockAlreadySoldException("Stock already sold");
                    }
                    holdingDTO.setNumShares(existingHolding.getNumShares() - holdingDTO.getNumShares());
        }
        Holdings holding = modelMapper.map(holdingDTO, Holdings.class);
        holding.setNumShares(0);
        holdingsRepo.save(holding);
        HoldingDTO updateHoldingDTO = modelMapper.map(holding, HoldingDTO.class);
        return updateHoldingDTO.getNumShares();
    }
    // Endpoint - 4
    public List<HoldingDTO> getAllHoldings(String accountId) {
        return holdingsRepo.findByAccountId(accountId).stream()
                .map(holding -> modelMapper.map(holding, HoldingDTO.class))
                .collect(Collectors.toList());
    }

}
