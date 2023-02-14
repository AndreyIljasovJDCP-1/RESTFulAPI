package ru.spring.restfull.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import ru.spring.restfull.exception.InvalidCredentials;
import ru.spring.restfull.model.Authorities;
import ru.spring.restfull.model.User;

import java.util.Arrays;

public class MyHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(UserParam.class);
    }

    @Override
    public User resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        if (supportsParameter(parameter)) {
            var login = webRequest.getParameter("login");
            var password = webRequest.getParameter("password");
            var authorities = webRequest.getParameterValues("authorities");
            if (isEmpty(login)) {
                throw new InvalidCredentials("@UserParam: поле Login не должно быть пустым");
            }
            if (isEmpty(password)) {
                throw new InvalidCredentials("@UserParam: поле Password не должно быть пустым");
            }
            if (isEmpty(authorities)) {
                throw new InvalidCredentials("@UserParam: поле Authorities не должно быть пустым");
            }

            var authoritiesList = Arrays.stream(authorities).map(Authorities::valueOf).toList();
            return new User(login, password, authoritiesList);
        } else {
            throw new InvalidCredentials("@UserParam: not supported parameters");
        }
    }
    private boolean isEmpty(String parameter) {
        return parameter == null || parameter.isEmpty();
    }

    private boolean isEmpty(String[] parameters) {
        return parameters == null || parameters.length==0;
    }
}
