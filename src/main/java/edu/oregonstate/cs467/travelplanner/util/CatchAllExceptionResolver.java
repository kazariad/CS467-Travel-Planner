package edu.oregonstate.cs467.travelplanner.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;

/**
 * {@link org.springframework.web.servlet.HandlerExceptionResolver HandlerExceptionResolver} implementation for handling any Exceptions not resolved by the default HandlerExceptionResolvers.
 * Should be registered at the end of the HandlerExceptionResolver chain.
 * <br>Specifically, handles Exceptions that:
 * <br>1. Were not handled by {@link org.springframework.web.bind.annotation.ExceptionHandler @ExceptionHandler} methods inside
 * {@link org.springframework.stereotype.Controller @Controller} or
 * {@link org.springframework.web.bind.annotation.ControllerAdvice @ControllerAdvice} classes
 * ({@link org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver ExceptionHandlerExceptionResolver}).
 * <br>2. Are not annotated with {@link org.springframework.web.bind.annotation.ResponseStatus @ResponseStatus} or do not extend
 * {@link org.springframework.web.server.ResponseStatusException ResponseStatusException}
 * ({@link org.springframework.web.servlet.mvc.annotation.ResponseStatusExceptionResolver ResponseStatusExceptionResolver}).
 * <br>3. Are not a standard Spring MVC exception
 * ({@link org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver DefaultHandlerExceptionResolver}).
 * <br>
 * <br>Note: Spring wraps Throwables into Exceptions.
 */
public class CatchAllExceptionResolver extends AbstractHandlerExceptionResolver {
    @Override
    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        try {
            logger.error("Unhandled exception", ex);
            // set error status so the response is dispatched to the servlet container's global error page
            response.sendError(500);
            // return empty ModelAndView to indicate that exception has been resolved and skip any further HandlerExceptionResolvers in the chain (if any)
            return new ModelAndView();
        } catch (Exception resolveEx) {
            logger.warn("Failure while trying to resolve exception [" + ex.getClass().getName() + "]", resolveEx);
            // return null to indicate that exception hasn't been resolved, and should be handled by the next HandlerExceptionResolver in the chain (if any)
            // if no further HandlerExceptionResolvers, exception will be rethrown and handled by the servlet container
            // which typically logs the exception and dispatches to the global error page
            return null;
        }
    }
}
