/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2014, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 *******************************************************************************/
package org.richfaces.tests.metamer.ftest.richMessages;

import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.message.Message;
import org.richfaces.fragment.messages.Messages;
import org.richfaces.fragment.messages.RichFacesMessages;
import org.richfaces.tests.metamer.ftest.abstractions.message.MessagesComponentTestPage;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class MessagesPage extends MessagesComponentTestPage {

    @FindBy(xpath = "//fieldset/span[contains(@id, 'messagesWithFor')]")
    private RichFacesMessages messagesComponentWithFor;
    @FindBy(xpath = "//fieldset/span[contains(@id, 'messagesWithGlobal')]")
    private RichFacesMessages messagesComponentWithGlobal;

    @Override
    public Messages<? extends Message> getMessagesComponentWithFor() {
        return messagesComponentWithFor;
    }

    @Override
    public Messages<? extends Message> getMessagesComponentWithGlobal() {
        return messagesComponentWithGlobal;
    }
}
