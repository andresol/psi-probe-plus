/*
 * Licensed under the GPL License.  You may not use this file except in
 * compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 *
 * THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF
 * MERCHANTIBILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 */
package com.googlecode.psiprobe.controllers.apps;

import com.googlecode.psiprobe.controllers.ContextHandlerController;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.catalina.Context;
import org.springframework.web.servlet.ModelAndView;

/**
 * Reloads application context.
 * 
 * @author Vlad Ilyushchenko
 * @author Mark Lewis
 */
public class AjaxReloadContextController extends ContextHandlerController {

    protected ModelAndView handleContext(String contextName, Context context,
                                             HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (!request.getContextPath().equals(contextName) && context != null) {
            try {
                logger.info(request.getRemoteAddr() + " requested RELOAD of "+contextName);
                context.reload();
            } catch (Throwable e) {
                logger.error(e);
                //
                // make sure we always re-throw ThreadDeath
                //
                if (e instanceof ThreadDeath) {
                    throw (ThreadDeath) e;
                }
            }
        }
        return new ModelAndView(getViewName(), "available",
                Boolean.valueOf(context != null && getContainerWrapper().getTomcatContainer().getAvailable(context)));
    }
}
