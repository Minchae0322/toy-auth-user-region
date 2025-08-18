package com.example.toyauth.app.external.controller;


import com.example.toyauth.app.external.controller.dto.UserExternalDto;
import com.example.toyauth.app.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "UserExternalController", description = "사용자 API")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/external/users")
public class UserExternalController {

  private final UserService userService;

  @GetMapping("/{userId}")
  @Operation(summary = "단일 활성 사용자 조회")
  public ResponseEntity<UserExternalDto> getActivateUserInfo(@PathVariable Long userId) {
    return ResponseEntity.ok(userService.getUserForExternal(userId));
  }

  @GetMapping("")
  @Operation(summary = "활성 사용자 목록 조회")
  public ResponseEntity<List<UserExternalDto>> getUserInfos(@RequestParam List<Long> userIds) {
    return ResponseEntity.ok(userService.getUsersForExternal(userIds));
  }

}
