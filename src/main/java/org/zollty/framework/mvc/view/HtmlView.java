/* 
 * Copyright (C) 2013-2015 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License").
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * Create by ZollTy on 2013-8-05 (http://blog.zollty.com, zollty@163.com)
 */
package org.zollty.framework.mvc.view;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.zollty.framework.core.config.IApplicationConfig;
import org.zollty.framework.mvc.View;
import org.zollty.framework.util.MvcUtils;

/**
 * @author zollty
 * @since 2013-8-05
 */
public class HtmlView implements View {

    private static String encoding;
    private final String text;

    public HtmlView(String text) {
        this.text = text;
    }

    @Override
    public void render(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setCharacterEncoding(getEncoding());
        response.setHeader("Content-Type", "text/html; charset=" + getEncoding());
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            writer.print(text);
        } finally {
            MvcUtils.IOUtil.closeIO(writer);
        }
    }

    public static void setEncoding(String encoding) {
        HtmlView.encoding = encoding;
    }

    public static String getEncoding() {
        if (HtmlView.encoding == null) {
            setEncoding(IApplicationConfig.DEFAULT_VIEW_ENCODING);
        }
        return encoding;
    }
}