package sd_009.bookstore.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import sd_009.bookstore.dto.jsonApiResource.error.ErrorObject;
import sd_009.bookstore.dto.response.generic.ApiErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import sd_009.bookstore.util.mapper.misc.ErrorMapper;

import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuthFailureHandler implements AuthenticationFailureHandler {
    private final ErrorMapper errorMapper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {

        log.error("Oauth failure ex handler", exception);

        errorMapper.writeFilterErrorDoc(response, HttpStatus.UNAUTHORIZED.value(), "Authentication failed", request.getRequestURI());

    }
}
