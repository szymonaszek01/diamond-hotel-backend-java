package com.app.diamondhotelbackend.controller;

import com.app.diamondhotelbackend.dto.userprofile.UpdateUserDetailsRequestDto;
import com.app.diamondhotelbackend.dto.userprofile.UserImageResponseDto;
import com.app.diamondhotelbackend.entity.UserProfile;
import com.app.diamondhotelbackend.security.jwt.JwtFilter;
import com.app.diamondhotelbackend.service.userprofile.UserProfileServiceImpl;
import com.app.diamondhotelbackend.util.Constant;
import com.app.diamondhotelbackend.util.UrlUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.multipart.MultipartFile;

import java.util.HexFormat;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(controllers = UserProfileController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class UserProfileControllerTests {

    @MockBean
    private UserProfileServiceImpl userProfileService;

    @MockBean
    private JwtFilter jwtFilter;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private UserProfile userProfile;

    private UserProfile updatedUserProfile;

    private List<UserProfile> userProfileList;

    private UpdateUserDetailsRequestDto updateUserDetailsRequestDto;

    private UserImageResponseDto userImageResponseDto;

    private UserImageResponseDto userImageResponseDtoAfterUpdate;

    private MockMultipartFile file;

    private static final String url = "/api/v1/user-profile";

    @BeforeEach
    public void init() {
        userProfile = UserProfile.builder()
                .id(1)
                .email("ala-gembala@wp.pl")
                .passportNumber("ZF005401499")
                .role(Constant.USER)
                .authProvider(Constant.LOCAL)
                .accountConfirmed(false)
                .picture(HexFormat.of().parseHex("e04fd020ea3a6910a2d808002b30309d"))
                .build();

        updatedUserProfile = UserProfile.builder()
                .id(1)
                .email("ala-gembala@wp.pl")
                .role(Constant.USER)
                .authProvider(Constant.LOCAL)
                .accountConfirmed(false)
                .picture(HexFormat.of().parseHex("e04fd020ea3a6910a2d808002b30309d"))
                .firstname("Ala")
                .lastname("Gembala")
                .age(21)
                .country("Poland")
                .passportNumber("ZF005401400")
                .phoneNumber("999111999")
                .city("Warsaw")
                .street("Adams street")
                .postalCode("09-783")
                .build();

        userProfileList = List.of(
                UserProfile.builder()
                        .id(1)
                        .email("ala-gembala@wp.pl")
                        .passportNumber("DF115499499")
                        .role(Constant.USER)
                        .authProvider(Constant.LOCAL)
                        .accountConfirmed(false)
                        .build(),
                UserProfile.builder()
                        .id(2)
                        .email("beata-pacanek@wp.pl")
                        .passportNumber("DF115499499")
                        .role(Constant.USER)
                        .authProvider(Constant.LOCAL)
                        .accountConfirmed(false)
                        .build()
        );

        updateUserDetailsRequestDto = UpdateUserDetailsRequestDto.builder()
                .firstname("Ala")
                .lastname("Gembala")
                .age(21)
                .country("Poland")
                .passportNumber("ZF005401400")
                .phoneNumber("999111999")
                .city("Warsaw")
                .street("Adams street")
                .postalCode("09-783")
                .build();

        userImageResponseDto = UserImageResponseDto.builder()
                .email(userProfile.getEmail())
                .image(userProfile.getPicture())
                .build();

        userImageResponseDtoAfterUpdate = UserImageResponseDto.builder()
                .email(userProfile.getEmail())
                .image(HexFormat.of().parseHex("a04fd020ea3a6910a2d808002b30309d"))
                .build();

        file = new MockMultipartFile(
                "image",
                "test.txt",
                "text/plain",
                HexFormat.of().parseHex("a04fd020ea3a6910a2d808002b30309d")
        );
    }

    @Test
    public void UserProfileController_GetUserProfileById_ReturnsUserProfile() throws Exception {
        when(userProfileService.getUserProfileById(Mockito.any(long.class))).thenReturn(userProfile);

        MockHttpServletRequestBuilder request = get(url + "/id/" + userProfile.getId() + "/details/info")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc
                .perform(request)
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(userProfile.getEmail())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.passport_number", CoreMatchers.is(userProfile.getPassportNumber())));
    }

    @Test
    public void UserProfileController_GetUserProfileByEmail_ReturnsUserProfile() throws Exception {
        when(userProfileService.getUserProfileByEmail(Mockito.any(String.class))).thenReturn(userProfile);

        MockHttpServletRequestBuilder request = get(url + "/email/" + UrlUtil.encode(userProfile.getEmail()) + "/details/info")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc
                .perform(request)
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(userProfile.getEmail())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.passport_number", CoreMatchers.is(userProfile.getPassportNumber())));
    }

    @Test
    public void UserProfileController_GetUserProfileList_ReturnsUserProfileList() throws Exception {
        when(userProfileService.getUserProfileList()).thenReturn(userProfileList);

        MockHttpServletRequestBuilder request = get(url + "/all/info")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc
                .perform(request)
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void UserProfileController_GetUserProfilePictureByEmail_ReturnsUserProfileList() throws Exception {
        when(userProfileService.getUserProfilePictureByEmail(Mockito.any(String.class))).thenReturn(userImageResponseDto);

        MockHttpServletRequestBuilder request = get(url + "/email/" + UrlUtil.encode(userProfile.getEmail()) + "/image")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc
                .perform(request)
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(userImageResponseDto.getEmail())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.image", CoreMatchers.is(objectMapper.writeValueAsString(userImageResponseDto.getImage()).replace("\"", ""))));
    }

    @Test
    public void UserProfileController_UpdateUserProfile_ReturnsUserProfileList() throws Exception {
        when(userProfileService.updateUserProfile(Mockito.any(String.class), Mockito.any(UpdateUserDetailsRequestDto.class))).thenReturn(updatedUserProfile);

        MockHttpServletRequestBuilder request = put(url + "/email/" + UrlUtil.encode(userProfile.getEmail()) + "/update/details")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateUserDetailsRequestDto));

        mockMvc
                .perform(request)
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(userImageResponseDto.getEmail())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstname", CoreMatchers.is(updateUserDetailsRequestDto.getFirstname())));
    }

    @Test
    public void UserProfileController_UpdateUserProfilePicture_ReturnsUserProfileList() throws Exception {
        when(userProfileService.updateUserProfilePicture(Mockito.any(MultipartFile.class), Mockito.any(String.class))).thenReturn(userImageResponseDtoAfterUpdate);

        MockHttpServletRequestBuilder request = multipart(url + "/email/" + UrlUtil.encode(userProfile.getEmail()) + "/update/image")
                .file(file);

        mockMvc
                .perform(request)
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void UserProfileController_DeleteUserProfile_ReturnsUserProfile() throws Exception {
        when(userProfileService.deleteUserProfile(Mockito.any(long.class))).thenReturn(userProfile);

        MockHttpServletRequestBuilder request = delete(url + "/id/" + userProfile.getId() + "/delete")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc
                .perform(request)
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(userProfile.getEmail())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.passport_number", CoreMatchers.is(userProfile.getPassportNumber())));
    }
}
