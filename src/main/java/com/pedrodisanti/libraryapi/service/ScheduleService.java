package com.pedrodisanti.libraryapi.service;

import com.pedrodisanti.libraryapi.model.entity.Loan;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private static final String CRON_LATE_LOANS = "0 0 0 1/1 * ?";

    private final LoanService loanService;
    private final EmailService emailService;

    @Scheduled(cron = CRON_LATE_LOANS)
    public  void sendMailToLateLoans(){
        List<Loan> allLateLoans = loanService.getAllLateLoans();
        List<String> mailsList = allLateLoans.stream().map(Loan::getCustomerEmail).collect(Collectors.toList());

        String message = "Should return book because it is late.";
        emailService.sendMails(message, mailsList);
    }
}
