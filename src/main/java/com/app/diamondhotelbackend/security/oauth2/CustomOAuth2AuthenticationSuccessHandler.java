package com.app.diamondhotelbackend.security.oauth2;

import com.app.diamondhotelbackend.entity.AuthToken;
import com.app.diamondhotelbackend.exception.UserProfileProcessingException;
import com.app.diamondhotelbackend.service.AuthTokenService;
import com.app.diamondhotelbackend.util.BaseUriPropertiesProvider;
import com.app.diamondhotelbackend.util.Constant;
import com.app.diamondhotelbackend.util.UrlUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class CustomOAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final AuthTokenService authTokenService;

    private final UserDetailsService userDetailsService;

    private final BaseUriPropertiesProvider baseUriPropertiesProvider;

    private final UrlUtil urlUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String callbackUri;

        try {
            CustomOAuth2User customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();
            callbackUri = createCallbackUriOnSuccess(customOAuth2User);

        } catch (UserProfileProcessingException e) {
            callbackUri = createCallbackUriOnFailure(e);
        }

        response.sendRedirect(callbackUri);
    }

    private String createCallbackUriOnSuccess(CustomOAuth2User customOAuth2User) throws UserProfileProcessingException {
        UserDetails userDetails = userDetailsService.loadUserByUsername(customOAuth2User.getEmail());
        AuthToken authToken = authTokenService.saveToken(userDetails);

        return UriComponentsBuilder.fromUriString(baseUriPropertiesProvider.getClient() + Constant.OAUTH2_CALLBACK_URI)
                .queryParam(Constant.OAUTH2_ATTR_ACCESS_TOKEN, urlUtil.encode(authToken.getAccessValue()))
                .queryParam(Constant.OAUTH2_ATTR_REFRESH_TOKEN, urlUtil.encode(authToken.getRefreshValue()))
                .queryParam(Constant.OAUTH2_ATTR_EMAIL, urlUtil.encode(authToken.getUserProfile().getEmail()))
                .queryParam(Constant.OAUTH2_ATTR_CONFIRMED, authToken.getUserProfile().isAccountConfirmed())
                .build()
                .toUriString();
    }

    private String createCallbackUriOnFailure(UserProfileProcessingException e) {
        return UriComponentsBuilder.fromUriString(baseUriPropertiesProvider.getClient() + Constant.OAUTH2_CALLBACK_URI)
                .queryParam(Constant.OAUTH2_ATTR_ERROR, urlUtil.encode(e.getMessage()))
                .build()
                .toUriString();
    }
}
