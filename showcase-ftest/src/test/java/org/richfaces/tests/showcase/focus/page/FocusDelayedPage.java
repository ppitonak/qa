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
package org.richfaces.tests.showcase.focus.page;

import static org.jboss.arquillian.graphene.Graphene.waitGui;
import static org.jboss.arquillian.graphene.Graphene.waitModel;

import java.util.concurrent.TimeUnit;

import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.test.selenium.support.ui.ElementIsFocused;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.showcase.focus.TestFocus;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class FocusDelayedPage extends FocusPage {

    @FindByJQuery("*[type=button]")
    public WebElement showPopupButton;

    @FindBy(className = ".rf-pp-cnt:visible")
    public WebElement popupContent;

    @FindByJQuery("*[value*=Save]")
    public WebElement saveButton;

    @FindByJQuery("*[value*=Cancel]")
    public WebElement cancelButton;

    public void showPopup() {
        showPopupButton.click();
        waitGui().until().element(saveButton).is().visible();
    }

    public void cancelPopup() {
        cancelButton.click();
        waitGui().until().element(saveButton).is().not().visible();
    }

    public void waitForFocusIsGiven(WebElement input) {
        waitModel().withTimeout(TestFocus.TIMEOUT_FOCUS, TimeUnit.SECONDS).until(new ElementIsFocused(input));
    }

}
