package ir.parian.loan.web.rest.controller;

import ir.parian.loan.service.MemberService;
import ir.parian.loan.service.dto.MemberDto;
import ir.parian.loan.web.rest.WebMapper;
import ir.parian.loan.web.rest.request.MemberRequest;
import ir.parian.loan.web.rest.response.MemberResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/member")
public class MemberController {
    private final WebMapper webMapper;
    private final MemberService memberService;

    public MemberController(final WebMapper webMapper,
                            final MemberService memberService) {
        this.webMapper = webMapper;
        this.memberService = memberService;
    }

    @GetMapping
    public Page<MemberResponse> getMembers(final Pageable pageRequest,
                                           final @RequestParam(required = false) String search) {
        return memberService.search(search, pageRequest)
                .map(webMapper::toMemberResponse);
    }

    @GetMapping(path = "{id}")
    public ResponseEntity<MemberResponse> getMember(final @PathVariable("id") Long id) {
        return ResponseEntity.of(memberService.findById(id).map(webMapper::toMemberResponse));
    }

    @PostMapping
    public void createMember(final @Valid @RequestBody MemberRequest request) {
        final MemberDto memberDto = webMapper.toMemberDto(request);
        memberService.saveOrUpdate(memberDto);
    }

    @PutMapping(path = "{id}")
    public void updateMember(final @PathVariable("id") Long id,
                             final @Valid @RequestBody MemberRequest request) {
        final MemberDto memberDto = webMapper.toMemberDto(id, request);
        memberService.saveOrUpdate(memberDto);
    }

    @DeleteMapping(path = "{id}")
    public void deleteMember(final @PathVariable("id") Long id) {
        memberService.deleteById(id);
    }
}
