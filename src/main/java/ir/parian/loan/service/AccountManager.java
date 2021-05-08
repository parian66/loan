package ir.parian.loan.service;

import com.ibm.icu.util.Calendar;
import ir.parian.loan.service.dto.MemberAccountDto;
import ir.parian.loan.service.enums.SystemAccountType;
import ir.parian.loan.service.utils.CalendarUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static ir.parian.loan.service.utils.BigDecimalUtil.hasNoValue;
import static ir.parian.loan.service.utils.BigDecimalUtil.hasValue;

@Component
public class AccountManager {
    private final AccountService accountService;

    public AccountManager(final AccountService accountService) {
        this.accountService = accountService;
    }

    @PostConstruct
    public void init() {
        Stream.of(SystemAccountType.values())
                .filter(type -> accountService.findSystemAccount(type).isEmpty())
                .forEach(accountService::createSystemAccount);

        iterateAllMemberAccounts(page -> page.stream()
                .filter(account -> hasValue(account.getMonthlyAmount()))
                .filter(account -> hasNoValue(account.getOverdueMonthlyAmount()))
                .map(MemberAccountDto::getId)
                .forEach(accountService::createMonthly));
    }

    @Scheduled(cron = "0 0 7 * *")
    public void createNewMonthlies() {
        if (CalendarUtil.getPersianCalendar().get(Calendar.DAY_OF_MONTH) != 1) {
            return;
        }

        iterateAllMemberAccounts(page -> page.stream()
                .filter(account -> hasValue(account.getMonthlyAmount()))
                .map(MemberAccountDto::getId)
                .forEach(accountService::createMonthly));
    }

    private void iterateAllMemberAccounts(Consumer<Page<MemberAccountDto>> consumer) {
        Page<MemberAccountDto> page = null;
        do {
            final Pageable request = Optional.ofNullable(page)
                    .map(Slice::nextPageable)
                    .orElse(PageRequest.of(0, 1000));
            page = accountService.loadMemberAccounts(request);
            consumer.accept(page);
        } while (page.hasNext());
    }
}
