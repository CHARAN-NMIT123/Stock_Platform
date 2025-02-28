package com.stocks.tradermanagement.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.stocks.tradermanagement.dtos.HoldingDTO;
import com.stocks.tradermanagement.dtos.OrderDTO;
import com.stocks.tradermanagement.service.HoldingService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;


@RestController
@RequestMapping("/api/holdings")
@SecurityRequirement(name = "Bearer Authentication")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class HoldingsController {

    @Autowired
    private HoldingService holdingService;
    
    @Autowired
    private RestTemplate restTemplate;

   @PostMapping("/buy/marketPlan")
   @PreAuthorize("hasAuthority('TRADER')")
   public ResponseEntity<String> buyMarketOrder(@RequestBody OrderDTO orderDTO, Authentication authentication) {
    int unitsBuy = holdingService.processMarketBuyOrder(orderDTO);
    System.out.println(orderDTO);
    if (unitsBuy > 0) {
        return ResponseEntity.ok("updated share: " + unitsBuy);
    } else {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to Buy order.");
    }
}
   @PostMapping("/sell/marketPlan") 
   @PreAuthorize("hasAuthority('TRADER')")
   public ResponseEntity<String> sellMarketOrder(@RequestBody OrderDTO orderDTO, Authentication authentication) {
    int unitsSold = holdingService.processMarketSellOrder(orderDTO);
    if (unitsSold > 0) {
        return ResponseEntity.ok("updated share: " + unitsSold);
    } else {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to sell order.");
    }
}
   @PostMapping("/sell/stopLoss") 
   @PreAuthorize("hasAuthority('RISKMANAGEMENT') || hasAuthority('TRADER')")
   public ResponseEntity<String> sellStopLossOrder(@RequestBody OrderDTO orderDTO, Authentication authentication) {
	   holdingService.processStopLossSellOrder(orderDTO);
    
        return ResponseEntity.ok("All units sold");
    
}
   @PostMapping("/buy/positionSizing") 
   @PreAuthorize("hasAuthority('RISKMANAGEMENT') || hasAuthority('TRADER')")
   public ResponseEntity<String> buyPositionSizingOrder(@RequestBody OrderDTO orderDTO, Authentication authentication) {
    int unitsSold = holdingService.processPostionSizingMarketOrder(orderDTO);
    if (unitsSold > 0) {
        return ResponseEntity.ok("Units sold: " + unitsSold);
    } else {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Sold");
    }
}

    @GetMapping("/{accountId}") 	
    @PreAuthorize("hasAuthority('STOCKADMIN') || hasAuthority('TRADER')")
    public List<HoldingDTO> getOrderById(@PathVariable String accountId, Authentication authentication) {

        return holdingService.getAllHoldings(accountId);
        // if (orderDTO != null) {
        //     return ResponseEntity.ok(List<orderDTO>);
        // } else {
        //     return ResponseEntity.notFound().build();
        // }
    }
    
    @GetMapping("/pull")
    @PreAuthorize("hasAuthority('STOCKADMIN')")
    public List<?>  getTradesFromLast5Days(@RequestParam String accountId, Authentication authentication){
    	String url = "http://localhost:9091/api/stock/trades/last5days?accountId=" + accountId;
    	return restTemplate.getForObject(url, List.class);
    }
}
