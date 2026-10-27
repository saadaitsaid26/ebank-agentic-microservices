package net.saad.ebankservice.services;

import net.saad.ebankservice.entities.BankAccount;
import net.saad.ebankservice.feign.CustomerRestClient;
import net.saad.ebankservice.model.Customer;
import net.saad.ebankservice.repository.BankAccountRepository;
import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.ai.mcp.annotation.McpToolParam;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class EBankService {
    private BankAccountRepository bankAccountRepository;
    private CustomerRestClient customerRestClient;

    EBankService(BankAccountRepository bankAccountRepository, CustomerRestClient customerRestClient){

        this.bankAccountRepository=bankAccountRepository;
        this.customerRestClient=customerRestClient;
    }

    @McpTool(description = "All Bank accounts list")
    public List<BankAccount> getAllBankAccounts() {
        List<BankAccount> accounts = bankAccountRepository.findAll();

        accounts.forEach(account -> {
            account.setCustomer(
                    customerRestClient.getCustomerById(account.getCustomerId())
            );
        });

        return accounts;
    }

    @McpTool(description = "Get a bank account by id")
    public BankAccount getBankAccountById(@McpToolParam(description = "The bank account id") String id){
        BankAccount bankAccount= bankAccountRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Bank Account Not found"));
        bankAccount.setCustomer(
                customerRestClient.getCustomerById(bankAccount.getCustomerId()));
        return bankAccount;
    }

    @McpTool(description = "Save a new bank account")
    public BankAccount save(@McpToolParam(description = "the bank account to save") BankAccount bankAccount){
        try {
            Customer customer = customerRestClient.getCustomerById(bankAccount.getCustomerId());
            bankAccount.setId(UUID.randomUUID().toString());
            bankAccount.setCreatedAt(new Date());
            return bankAccountRepository.save(bankAccount);
        }catch(Exception e){
            throw new RuntimeException(e.getMessage());
        }

    }
}
