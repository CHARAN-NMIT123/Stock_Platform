package com.stocks.stocksexchange.serviceTests;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.web.client.RestTemplate;

import com.stocks.stocksexchange.dtos.AccountDTO;
import com.stocks.stocksexchange.entities.Account;
import com.stocks.stocksexchange.repositories.AccountRepo;
import com.stocks.stocksexchange.services.AccountService;
import com.stocks.stocksexchange.services.PortfolioService;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {
	@Mock
	private AccountRepo accountRepo;
	
	@Mock
	private PortfolioService portfolioService;
	
	@Mock
	private ModelMapper modelMapper;
	
	@Mock
	private RestTemplate restTemplate;
	
	@InjectMocks
	private AccountService accountService;
	
	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}
	
	
	@Test
	void testGetUserById() {
		Account mockAccount = new Account("DA3606","Arka","Das","arka@cognizant.com",true,"Arka Das");
		AccountDTO mockAccountDTO = new AccountDTO("DA3606","Arka","Das","arka@cognizant.com",true,"Arka Das");
		Mockito.when(accountRepo.findById("DA3606")).thenReturn(Optional.of(mockAccount));
		Mockito.when(modelMapper.map(mockAccount, AccountDTO.class)).thenReturn(mockAccountDTO);
		
		AccountDTO accountDTO = accountService.getAccountById("DA3606");
		
		Assertions.assertNotNull(accountDTO);
		Assertions.assertEquals("Arka", accountDTO.getFname());
		Mockito.verify(accountRepo).findById("DA3606");
		Mockito.verify(modelMapper).map(mockAccount, AccountDTO.class);
	}
	
	@Test
    void testRegisterAccount() {
        // Mock data
        AccountDTO mockAccountDTO = new AccountDTO("DA3606", "Arka", "Das", "arka@cognizant.com", true, "Arka Das");
        Account mockAccount = new Account("DA3606", "Arka", "Das", "arka@cognizant.com", true, "Arka Das");
        
        // Mocking modelMapper and accountRepo methods
        Mockito.when(modelMapper.map(mockAccountDTO, Account.class)).thenReturn(mockAccount);
        Mockito.when(accountRepo.save(mockAccount)).thenReturn(mockAccount);
        Mockito.when(modelMapper.map(mockAccount, AccountDTO.class)).thenReturn(mockAccountDTO);

        // Calling the service method
        AccountDTO updatedAccountDTO = accountService.registerAccount(mockAccountDTO);

        // Assertions to verify the expected behavior
        Assertions.assertNotNull(updatedAccountDTO);
        Assertions.assertEquals("Arka", updatedAccountDTO.getFname());
        Mockito.verify(modelMapper).map(mockAccountDTO, Account.class);
        Mockito.verify(accountRepo).save(mockAccount);
        Mockito.verify(modelMapper).map(mockAccount, AccountDTO.class);
    }

	@Test
    void testUpdateAccount() {
        // Mock data
        AccountDTO mockAccountDTO = new AccountDTO("DA3606", "Arka", "Das", "arka@cognizant.com", true, "Arka Das");
        Account mockAccount = new Account("DA3606", "Arka", "Das", "arka@cognizant.com", true, "Arka Das");

        // Mocking modelMapper and accountRepo methods
        Mockito.when(modelMapper.map(mockAccountDTO, Account.class)).thenReturn(mockAccount);
        Mockito.when(accountRepo.save(mockAccount)).thenReturn(mockAccount);
        Mockito.when(modelMapper.map(mockAccount, AccountDTO.class)).thenReturn(mockAccountDTO);

        // Calling the service method
        AccountDTO updatedAccountDTO = accountService.updateAccount(mockAccountDTO, null);

        // Assertions to verify the expected behavior
        Assertions.assertNotNull(updatedAccountDTO);
        Assertions.assertEquals("Arka", updatedAccountDTO.getFname());
        Mockito.verify(modelMapper).map(mockAccountDTO, Account.class);
        Mockito.verify(accountRepo).save(mockAccount);
        Mockito.verify(modelMapper).map(mockAccount, AccountDTO.class);
    }
	
	@Test
    void testUpdateAccountWithNullId() {
        AccountDTO mockAccountDTO = new AccountDTO(null, "Arka", "Das", "arka@cognizant.com", true, "Arka Das");

        // Assertions to verify that an exception is thrown
        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
            accountService.updateAccount(mockAccountDTO, null);
        });

        Assertions.assertEquals("Account not found", exception.getMessage());
    }
	
	 @Test
	    void testGetAllAccounts() {
	        // Mock data
	        Account mockAccount1 = new Account("DA3606", "Arka", "Das", "arka@cognizant.com", true, "Arka Das");
	        Account mockAccount2 = new Account("DA3607", "John", "Doe", "john@cognizant.com", true, "John Doe");
	        AccountDTO mockAccountDTO1 = new AccountDTO("DA3606", "Arka", "Das", "arka@cognizant.com", true, "Arka Das");
	        AccountDTO mockAccountDTO2 = new AccountDTO("DA3607", "John", "Doe", "john@cognizant.com", true, "John Doe");

	        // Mocking accountRepo.findAll and modelMapper.map methods
	        Mockito.when(accountRepo.findAll()).thenReturn(Arrays.asList(mockAccount1, mockAccount2));
	        Mockito.when(modelMapper.map(mockAccount1, AccountDTO.class)).thenReturn(mockAccountDTO1);
	        Mockito.when(modelMapper.map(mockAccount2, AccountDTO.class)).thenReturn(mockAccountDTO2);

	        // Calling the service method
	        List<AccountDTO> accountDTOList = accountService.getAllAccounts();

	        // Assertions to verify the expected behavior
	        Assertions.assertNotNull(accountDTOList);
	        Assertions.assertEquals(2, accountDTOList.size());
	        Assertions.assertEquals("Arka", accountDTOList.get(0).getFname());
	        Assertions.assertEquals("John", accountDTOList.get(1).getFname());
	        Mockito.verify(accountRepo).findAll();
	        Mockito.verify(modelMapper).map(mockAccount1, AccountDTO.class);
	        Mockito.verify(modelMapper).map(mockAccount2, AccountDTO.class);
	    }
}


	

