package ir.parian.loan.web.rest.controller;

import ir.parian.loan.service.LoanService;
import ir.parian.loan.service.dto.LoanDto;
import ir.parian.loan.service.dto.LoanFilterDto;
import ir.parian.loan.service.dto.NewLoanDto;
import ir.parian.loan.service.enums.LoanStatus;
import ir.parian.loan.web.rest.WebMapper;
import ir.parian.loan.web.rest.request.NewLoanRequest;
import ir.parian.loan.web.rest.request.RepayLoanRequest;
import ir.parian.loan.web.rest.response.LoanResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/loan")
public class LoanController {
    private final WebMapper webMapper;
    private final LoanService loanService;

    public LoanController(final WebMapper webMapper, final LoanService loanService) {
        this.webMapper = webMapper;
        this.loanService = loanService;
    }

    @GetMapping
    public Page<LoanResponse> getLoans(final Pageable pageRequest,
                                       final @RequestParam(required = false) Long memberId,
                                       final @RequestParam(required = false) LoanStatus status) {
        final LoanFilterDto loanFilterDto = new LoanFilterDto(memberId, status);
        return loanService.search(loanFilterDto, pageRequest)
                .map(webMapper::toLoanResponse);
    }

    @GetMapping(path = "{id}")
    public ResponseEntity<LoanResponse> getLoan(final @PathVariable Long id) {
        return ResponseEntity.of(loanService.findByLoanId(id)
                .map(loanDto -> webMapper.toLoanResponse(loanDto, loanService.findInstallmentsByLoadId(id))));
    }

    @PostMapping
    public LoanResponse createLoan(final @Valid @RequestBody NewLoanRequest newLoanRequest) {
        final NewLoanDto newLoanDto = webMapper.toNewLoanDto(newLoanRequest);
        final LoanDto loanDto = loanService.create(newLoanDto);
        return webMapper.toLoanResponse(loanDto);
    }

    @PutMapping(path = "{id}/repay")
    public void repayLoan(final @PathVariable Long id, final @RequestBody RepayLoanRequest request) {
        loanService.repay(id, request.getCount());
    }
}
