package com.daria.javatemplate.admin.domain.domainName.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(path = "/admin/v1/scheduler-example", produces = "application/json")
@RequiredArgsConstructor
@EnableAsync
public class SchedulerExampleController {
    //mapping시 직접 접근 가능

    //    해당 메서드가 끝나는 시간 기준, milliseconds 간격으로 실행
    //    하나의 인스턴스만 항상 실행되도록 해야 할 상황에서 유용
    // @Scheduled(fixedDelayString = "${fixedDelay.in.milliseconds}") // 문자열 milliseconds 사용 시
    @GetMapping("/fixed-delay")
    @Scheduled(fixedDelay = 1000)
    public void scheduleFixedDelayTask() throws InterruptedException {
        log.info("Fixed delay task - {}", System.currentTimeMillis() / 1000);
        Thread.sleep(5000);
    }

    //    해당 메서드가 시작하는 시간 기준, milliseconds 간격으로 실행
    //    병렬로 Scheduler 를 사용할 경우, Class에 @EnableAsync, Method에 @Async 추가
    //    모든 실행이 독립적인 경우에 유용
    // @Scheduled(fixedRateString = "${fixedRate.in.milliseconds}")  // 문자열 milliseconds 사용 시
    @Async
    @GetMapping("/fixed-rate")
    @Scheduled(fixedRate = 1000)
    public void scheduleFixedRateTask() throws InterruptedException {
        log.info("Fixed rate task - {}", System.currentTimeMillis() / 1000);
        Thread.sleep(5000);
    }

    //    initialDelay 값 이후 처음 실행 되고, fixedDelay 값에 따라 계속 실행
    @GetMapping("/mixed-fixed")
    @Scheduled(fixedDelay = 1000, initialDelay = 5000)
    public void scheduleFixedRateWithInitialDelayTask() {
        long now = System.currentTimeMillis() / 1000;
        log.info("Fixed rate task with one second initial delay - {}", now);
    }

    // 작업 예약으로 실행
    // @Scheduled(cron = "0 15 10 15 11 ?") // 11월 15일 오전 10시 15분에 실행
    // @Scheduled(cron = "${cron.expression}")
    // @Scheduled(cron = "0 15 10 15 * ?", zone = "Europe/Paris") // timezone 설정
    @Scheduled(cron = "0 15 10 15 * ?") // 매월 15일 오전 10시 15분에 실행
    @GetMapping("/cron")
    public void scheduleTaskUsingCronExpression() {
        long now = System.currentTimeMillis() / 1000;
        log.info("schedule tasks using cron jobs - {}", now);
    }
}
