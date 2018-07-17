package com.bpwizard.myresources.api;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bpwizard.myresources.ApplicationConstants;
import com.bpwizard.myresources.api.exception.InvalidRequestException;
import com.bpwizard.myresources.dto.UserData;
import com.bpwizard.myresources.dto.UserWithToken;
import com.bpwizard.myresources.resource.user.EncryptService;
import com.bpwizard.myresources.resource.user.User;
import com.bpwizard.myresources.resource.user.UserRepository;
import com.bpwizard.myresources.service.application.UserQueryService;
import com.bpwizard.myresources.service.core.JwtService;
import com.fasterxml.jackson.annotation.JsonRootName;

import lombok.Getter;
import lombok.NoArgsConstructor;

// @RestController
// @RequestMapping(ApplicationConstants.API_BASE_PATH + "/users")
public class UsersApi {
    private UserRepository userRepository;
    private UserQueryService userQueryService;
    private String defaultImage;
    private EncryptService encryptService;
    private JwtService jwtService;

    //@AuthenticationPrincipal Principal principal
    // @Autowired
    public UsersApi(UserRepository userRepository,
                    UserQueryService userQueryService,
                    EncryptService encryptService,
                    @Value("${image.default}") String defaultImage,
                    JwtService jwtService) {
        this.userRepository = userRepository;
        this.userQueryService = userQueryService;
        this.encryptService = encryptService;
        this.defaultImage = defaultImage;
        this.jwtService = jwtService;
    }

    // @PostMapping()
    public ResponseEntity createUser(@Valid @RequestBody RegisterParam registerParam, BindingResult bindingResult) {
        checkInput(registerParam, bindingResult);

        User user = new User(
            registerParam.getEmail(),
            registerParam.getUsername(),
            encryptService.encrypt(registerParam.getPassword()),
            "",
            defaultImage);
        userRepository.save(user);
        UserData userData = userQueryService.findById(user.getId()).get();
        return ResponseEntity.status(201).body(userResponse(new UserWithToken(userData, jwtService.toToken(user))));
    }

    private void checkInput(@Valid @RequestBody RegisterParam registerParam, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new InvalidRequestException(bindingResult);
        }
        if (userRepository.findByUsername(registerParam.getUsername()).isPresent()) {
            bindingResult.rejectValue("username", "DUPLICATED", "duplicated username");
        }

        if (userRepository.findByEmail(registerParam.getEmail()).isPresent()) {
            bindingResult.rejectValue("email", "DUPLICATED", "duplicated email");
        }

        if (bindingResult.hasErrors()) {
            throw new InvalidRequestException(bindingResult);
        }
    }

    // @PostMapping(path = "/login")
    public ResponseEntity userLogin(@Valid @RequestBody LoginParam loginParam, BindingResult bindingResult) {
        Optional<User> optional = userRepository.findByEmail(loginParam.getEmail());
        if (optional.isPresent() && encryptService.check(loginParam.getPassword(), optional.get().getPassword())) {
            UserData userData = userQueryService.findById(optional.get().getId()).get();
            return ResponseEntity.ok(userResponse(new UserWithToken(userData, jwtService.toToken(optional.get()))));
        } else {
            bindingResult.rejectValue("password", "INVALID", "invalid email or password");
            throw new InvalidRequestException(bindingResult);
        }
    }

    private Map<String, Object> userResponse(UserWithToken userWithToken) {
        return new HashMap<String, Object>() {{
            put("user", userWithToken);
        }};
    }
}

@Getter
@JsonRootName("user")
@NoArgsConstructor
class LoginParam {
    @NotBlank(message = "can't be empty")
    @Email(message = "should be an email")
    private String email;
    @NotBlank(message = "can't be empty")
    private String password;
}

@Getter
@JsonRootName("user")
@NoArgsConstructor
class RegisterParam {
    @NotBlank(message = "can't be empty")
    @Email(message = "should be an email")
    private String email;
    @NotBlank(message = "can't be empty")
    private String username;
    @NotBlank(message = "can't be empty")
    private String password;
}
