package org.revo.Service;

import org.revo.domain.Child;
import org.revo.model.Email;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by revo on 05/11/15.
 */
@Service
public interface Util {
    void email(Email email);

    public void CreateQueueFor(String name);

    void send();

    void afteruplode(Child child);

    String getCurrentLogin();

    void handelSocialSignUp(WebRequest request, HttpServletRequest req, HttpServletResponse res);

}
