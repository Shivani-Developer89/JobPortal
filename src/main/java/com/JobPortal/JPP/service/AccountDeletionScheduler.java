package com.JobPortal.JPP.service;

import com.JobPortal.JPP.entity.User;
import com.JobPortal.JPP.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountDeletionScheduler {

    private final UserRepository userRepository;

    @Scheduled(cron = "0 0 2 * * *")
    @Transactional
    public void permanentlyDeleteExpiredAccounts() {

        LocalDateTime deadline =
                LocalDateTime.now().minusDays(30);

        List<User> users =
                userRepository.findByDeletionRequestedTrueAndDeletionRequestedAtBefore(
                        deadline
                );

        for (User user : users) {
            userRepository.delete(user);
        }
    }
}