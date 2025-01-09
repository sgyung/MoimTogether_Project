package com.project.moimtogether.controller;

import com.project.moimtogether.config.error.ErrorResponse;
import com.project.moimtogether.config.error.customException.SuccessCode;
import com.project.moimtogether.dto.meeting.PostMeetingResDTO;
import com.project.moimtogether.dto.member.*;
import com.project.moimtogether.jwt.AuthToken;
import com.project.moimtogether.service.member.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "[회원가입]")
public class UserApiController {

    private final UserService userService;

    // 회원가입 컨트롤러
    @PostMapping("/sign-up")
    @Operation(summary = "회원가입을 진행합니다.")
    public ResponseEntity<ErrorResponse<PostMemberResDTO>> signUp(@Valid @RequestBody PostMemberReqDTO postUserReqDTO){
        PostMemberResDTO postUserResDTO = userService.signUp(postUserReqDTO);
        return ResponseEntity.ok(new ErrorResponse<>(SuccessCode.REGISTER_INSERT_SUCCESS,postUserResDTO));
    }

    // 로그인 컨트롤러
    @PostMapping("/login")
    @Operation(summary = "로그인을 진행하고, 유효성 검증을 합니다.")
    public ResponseEntity<ErrorResponse<String>> login(@RequestBody LoginDTO loginDTO){

        List<AuthToken> authTokenList = userService.login(loginDTO);

        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        for (AuthToken authToken : authTokenList) {
            if ("AccessToken".equals(authToken.getTokenType())) {
                headers.add("Authorization", "Bearer " + authToken.getToken());
            } else if ("RefreshToken".equals(authToken.getTokenType())) {
                headers.add("RefreshToken", "Bearer " + authToken.getToken());
            }
        }

        return ResponseEntity.ok()
                .headers(headers)
                .body(new ErrorResponse<>(SuccessCode.COMPLETE_LOGIN_SUCCESS,"로그인 성공"));
    }

    // 모든 회원 조회 컨트롤러
    @GetMapping("/users")
    @Operation(summary = "모든 회원을 조회합니다.")
    public ResponseEntity<ErrorResponse<List<BaseMemberDTO>>> searchUserList(){

        List<BaseMemberDTO> userList = userService.searchMemberListService();

        return ResponseEntity.ok(new ErrorResponse<>(SuccessCode.COMPLETE_SEARCH_SUCCESS,userList));
    }

    // 특정 회원 조회 컨트롤러
    @GetMapping("/users/{id}")
    @Operation(summary = "특정회원을 조회 합니다.")
    public ResponseEntity<ErrorResponse<MemberInfoDTO>> searchUser(@PathVariable("id") Long id){

        MemberInfoDTO userInfoDTO = userService.searchMemberService(id);

        return ResponseEntity.ok(new ErrorResponse<>(SuccessCode.COMPLETE_SEARCH_SUCCESS,userInfoDTO));
    }

    // 마이페이지 수정 컨트롤러
    @PatchMapping("/profile")
    @Operation(summary = "마이페이지를 수정합니다.")
    public ResponseEntity<ErrorResponse<PostMemberResDTO>> updateProfile(
            @Valid @RequestPart ModifyMemberReqDTO modifyUserReqDTO,
            @RequestPart(required = false) MultipartFile profileImage) throws IOException {

        PostMemberResDTO postUserResDTO = userService.modifyService(modifyUserReqDTO, profileImage);

        return ResponseEntity.ok(new ErrorResponse<>(SuccessCode.COMPLETE_MODIFY_SUCCESS,postUserResDTO));
    }

    // 마이페이지 이동 컨트롤러
    @GetMapping("/profile")
    @Operation(summary = "마이페이지를 조회합니다.")
    public ResponseEntity<ErrorResponse<PostMemberResDTO>> myProfile(){

        PostMemberResDTO resDTO = userService.myProfile();

        return ResponseEntity.ok(new ErrorResponse<>(SuccessCode.COMPLETE_SEARCH_SUCCESS,resDTO));
    }

    // 아이디 찾기 컨트롤러
    @PostMapping("/users/id")
    @Operation(summary = "회원 정보를 이용하여 아이디를 찾습니다.")
    public ResponseEntity<ErrorResponse<String>> findLoginId(@Valid @RequestBody FindLoginIdReqDTO findLoginIdReqDTO){
        String findId = userService.findLoginIdService(findLoginIdReqDTO);

        if(findId == null){
            ErrorResponse<String> errorResponse = new ErrorResponse<>(false, "아이디를 찾을 수 없습니다.", 404);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }

        return ResponseEntity.ok(new ErrorResponse<>(SuccessCode.COMPLETE_SEARCH_SUCCESS,"아이디 : " + findId));
    }

    // 비밀번호 찾기 컨트롤러
    @PostMapping("/users/password")
    @Operation(summary = "회원정보를 이용하여 임시 비밀번호를 발급합니다.")
    public ResponseEntity<ErrorResponse<String>> updatePassword(@Valid @RequestBody FindPasswordReqDTO findPasswordReqDTO){
        userService.sendCodeToEmail(findPasswordReqDTO);
        return ResponseEntity.ok(new ErrorResponse<>(true,"이메일 전송 성공",200));
    }

    // 마이페이지 비밀번호 수정 컨트롤러
    @PatchMapping("/profile/password")
    @Operation(summary = "발급받은 임시 비밀번호를 이용해서 비밀번호를 재설정합니다.")
    public ResponseEntity<ErrorResponse<String>> updatePassword(
            @Valid @RequestBody PasswordVerificationReqDTO passwordVerificationReqDTO) {

        userService.regeneratePasswordService(passwordVerificationReqDTO);

        return ResponseEntity.ok(new ErrorResponse<>(true,"비밀번호 재설정 성공",200));
    }

    // 회원 모임 조회
    @GetMapping("/profile/meetings")
    @Operation(summary = "회원이 가입한 모임을 조회합니다.")
    public ResponseEntity<ErrorResponse<List<PostMeetingResDTO>>> searchMeetingsByMember() {

        List<PostMeetingResDTO> resDTO = userService.findMeetingList();

        return ResponseEntity.ok(new ErrorResponse<>(SuccessCode.COMPLETE_SEARCH_SUCCESS,resDTO));
    }

    // 회원 모임 탈회
    @DeleteMapping("/profile/meetings/{meetingId}")
    @Operation(summary = "회원이 모임을 탈퇴합니다.")
    public ResponseEntity<ErrorResponse<String>> withdrawMeetings(@PathVariable("meetingId") Long meetingId) {

        userService.withdrawMeeting(meetingId);

        return ResponseEntity.ok(new ErrorResponse<>(SuccessCode.COMPLETE_DELETE_SUCCESS,"성공"));
    }


}
