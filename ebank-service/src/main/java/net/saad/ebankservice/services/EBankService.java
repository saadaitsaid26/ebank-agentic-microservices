package net.saad.ebankservice.services;

import net.saad.ebankservice.entities.BankAccount;
import net.saad.ebankservice.feign.CustomerRestClient;
import net.saad.ebankservice.model.Customer;
import net.saad.ebankservice.repository.BankAccountRepository;
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


    public List<BankAccount> getAllBankAccounts() {
        List<BankAccount> accounts = bankAccountRepository.findAll();

        accounts.forEach(account -> {
            account.setCustomer(
                    customerRestClient.getCustomerById(account.getCustomerId())
            );
        });

        return accounts;
    }


    public BankAccount getBankAccountById(String id){
        BankAccount bankAccount= bankAccountRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Bank Account Not found"));
        bankAccount.setCustomer(
                customerRestClient.getCustomerById(bankAccount.getCustomerId()));
        return bankAccount;
    }

    public BankAccount save(BankAccount bankAccount){
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
