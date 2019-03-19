package com.example.atrs.member.api;

import com.example.atrs.member.Member;
import com.example.atrs.member.MemberService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping(path = "members")
public class MemberRestController {
	private final MemberService memberService;

	public MemberRestController(MemberService memberService) {
		this.memberService = memberService;
	}

	@GetMapping(path = "{id}")
	public ResponseEntity<?> get(@PathVariable("id") String membershipNumber) {
		Member member = this.memberService.findByMembershipNumber(membershipNumber);
		if (member == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(member);
	}

	@PostMapping(path = "me")
	public ResponseEntity<?> create(@RequestBody Member member,
			UriComponentsBuilder builder) {
		Member created = this.memberService.register(member);
		return ResponseEntity.created(builder.build().toUri()).body(created);
	}

	@GetMapping(path = "me")
	public ResponseEntity<?> getMe(@AuthenticationPrincipal Jwt jwt) {
		Member member = this.memberService.findByMembershipNumber(jwt.getSubject());
		return ResponseEntity.ok(member);
	}

	@PutMapping(path = "me")
	public ResponseEntity<?> updateMe(@AuthenticationPrincipal Jwt jwt,
			@RequestBody Member member) {
		member.setMembershipNumber(jwt.getSubject());
		this.memberService.update(member, member.getCurrentPassword());
		return ResponseEntity.ok(member);
	}
}
