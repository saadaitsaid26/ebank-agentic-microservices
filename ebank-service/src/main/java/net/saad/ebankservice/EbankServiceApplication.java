package net.saad.ebankservice;

import net.saad.ebankservice.entities.BankAccount;
import net.saad.ebankservice.services.EBankService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class EbankServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EbankServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner run(EBankService eBankService){
        return args -> {
          for(int i=1; i<5; i++){
              for(int j=0; j<5; j++){
                  eBankService.save(BankAccount.builder()
                          .type(Math.random()>0.5 ? "CurrentAccount" : "SavingAccount")
                          .balance(1000 + Math.random()*60000)
                          .customerId(i)
                          .build());
              }
          }
        };
    }
}
