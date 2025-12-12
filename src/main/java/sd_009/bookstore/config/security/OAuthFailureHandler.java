package sd_009.bookstore.config.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import sd_009.bookstore.util.mapper.misc.ErrorMapper;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuthFailureHandler implements AuthenticationFailureHandler {
    private final ErrorMapper errorMapper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {

        log.error("Oauth failure ex handler", exception);

        errorMapper.writeFilterErrorDoc(response, HttpStatus.UNAUTHORIZED.value(), "Authentication failed", request.getRequestURI());
//        response.setStatus(HttpStatus.UNAUTHORIZED.value());
//        response.setContentType("application/json");
//        response.getWriter().write(
//                "{\"error\": \"Authentication failed: " + exception.getMessage() + "\"}"
//        );
    }
}
