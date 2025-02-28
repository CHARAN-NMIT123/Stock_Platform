package com.stocks.stocksexchange.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.stocks.stocksexchange.dtos.OrderDTO;
import com.stocks.stocksexchange.dtos.TradesDTO;

@Configuration
public class ModelMapperConfig {
    
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        
        modelMapper.getConfiguration()
            .setPropertyCondition(context -> context.getSource() != null)
            .setSkipNullEnabled(true)
            .setMatchingStrategy(MatchingStrategies.STRICT);

        // Custom mapping for Portfolio -> PortfolioDTO
        // modelMapper.createTypeMap(Portfolio.class, PortfolioDTO.class)
        //     .addMappings(mapper -> 
        //         mapper.map(src -> src.getAccount().getAccountId(),
        //             PortfolioDTO::setAccountId));
        modelMapper.createTypeMap(OrderDTO.class, TradesDTO.class)
                   .addMappings(mapper -> {
                       mapper.map(OrderDTO::getOrderId, TradesDTO::setTradeId);
                       mapper.map(OrderDTO::getEntryPrice, TradesDTO::setTradedAt);
                   });
        

        return modelMapper;
    }
}