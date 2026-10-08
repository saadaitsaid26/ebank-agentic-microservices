package net.saad.ebankservice.services;

import net.saad.ebankservice.entities.BankAccount;
import net.saad.ebankservice.repository.BankAccountRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class EBankService {
    private BankAccountRepository bankAccountRepository;

    EBankService(BankAccountRepository bankAccountRepository){
        this.bankAccountRepository=bankAccountRepository;
    }

    public List<BankAccount> getAllBankAccounts(){
        return bankAccountRepository.findAll();
    }

    public BankAccount getBankAccountById(String id){
        return bankAccountRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Bank NOt found"));
    }

    public BankAccount save(BankAccount bankAccount){
        bankAccount.setId(UUID.randomUUID().toString());
        bankAccount.setCreatedAt(new Date());
        return bankAccountRepository.save(bankAccount);
    }
}
