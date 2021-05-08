package ir.parian.loan.web.rest.controller;

import ir.parian.loan.service.AccountService;
import ir.parian.loan.service.dto.*;
import ir.parian.loan.web.rest.WebMapper;
import ir.parian.loan.web.rest.request.NewMemberAccountRequest;
import ir.parian.loan.web.rest.request.NewTransactionRequest;
import ir.parian.loan.web.rest.request.PayMonthlyRequest;
import ir.parian.loan.web.rest.request.UpdateAccountRequest;
import ir.parian.loan.web.rest.response.MemberAccountResponse;
import ir.parian.loan.web.rest.response.MonthlyResponse;
import ir.parian.loan.web.rest.response.TransactionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/account")
public class AccountController {
    private final WebMapper webMapper;
    private final AccountService accountService;

    public AccountController(final WebMapper webMapper,
                             final AccountService accountService) {
        this.webMapper = webMapper;
        this.accountService = accountService;
    }

    @GetMapping
    public Page<MemberAccountResponse> getMemberAccounts(final Pageable pageRequest) {
        return accountService.loadMemberAccounts(pageRequest)
                .map(webMapper::toMemberAccountResponse);
    }

    @GetMapping(path = "{memberAccountId}")
    public ResponseEntity<MemberAccountResponse> getMember(final @PathVariable Long memberAccountId) {
        return ResponseEntity.of(accountService.findMemberAccountById(memberAccountId)
                .map(webMapper::toMemberAccountResponse));
    }

    @PostMapping
    public MemberAccountResponse createMemberAccounts(final @Valid @RequestBody NewMemberAccountRequest request) {
        final NewMemberAccountDto dto = webMapper.toNewMemberAccountDto(request);
        final MemberAccountDto memberAccount = accountService.createMemberAccount(dto);
        return webMapper.toMemberAccountResponse(memberAccount);
    }

    @PostMapping(path = "{memberAccountId}")
    public MemberAccountResponse updateMemberAccounts(final @PathVariable Long memberAccountId,
                                                      final @Valid @RequestBody UpdateAccountRequest request) {
        final UpdateAccountDto dto = webMapper.toUpdateAccountDto(memberAccountId, request);
        final MemberAccountDto memberAccount = accountService.updateMemberAccount(dto);
        return webMapper.toMemberAccountResponse(memberAccount);
    }

    @GetMapping(path = "{memberAccountId}/transaction")
    public Page<TransactionResponse> getTransactions(final @PathVariable Long memberAccountId,
                                                     final Pageable pageRequest) {
        return accountService.loadTransactions(memberAccountId, pageRequest)
                .map(webMapper::toTransactionResponse);
    }

    @PostMapping(path = "{memberAccountId}/transaction")
    public void createTransaction(final @PathVariable Long memberAccountId,
                                  final @Valid @RequestBody NewTransactionRequest request) {
        NewTransactionDto dto = webMapper.toNewTransactionDto(request, memberAccountId);
        accountService.createTransaction(dto);
    }

    @GetMapping(path = "{memberAccountId}/monthly")
    public Page<MonthlyResponse> getMonthlies(final @PathVariable Long memberAccountId,
                                              final Pageable pageRequest) {
        return accountService.loadMonthlies(memberAccountId, pageRequest)
                .map(webMapper::toMonthlyResponse);
    }

    @PutMapping(path = "{memberAccountId}/monthly/{monthlyId}/pay")
    public void payMonthly(final @PathVariable Long memberAccountId,
                           final @PathVariable Long monthlyId,
                           final @Valid @RequestBody PayMonthlyRequest request) {
        final PayMonthlyDto dto = webMapper.toPayMonthlyDto(memberAccountId, monthlyId, request);
        accountService.payMonthly(dto);
    }
}
