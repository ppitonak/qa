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
package org.richfaces.tests.metamer.ftest.richAutocomplete;

import static org.jboss.test.selenium.support.url.URLUtils.buildUrl;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.net.URL;

import org.jboss.arquillian.graphene.Graphene;
import org.openqa.selenium.support.FindBy;
import org.richfaces.fragment.autocomplete.RichFacesAutocomplete;
import org.richfaces.fragment.autocomplete.SelectOrConfirm;
import org.richfaces.fragment.common.picker.ChoicePickerHelper;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 * @author <a href="https://community.jboss.org/people/ppitonak">Pavol Pitonak</a>
 */
@IssueTracking("https://issues.jboss.org/browse/RF-13309")
public class TestAutocompleteFragment extends AbstractAutocompleteTest {

    @FindBy(css = "div[id$=autocomplete]")
    private RichFacesAutocomplete autocomplete;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richAutocomplete/ajaxOnSelectitem.xhtml");
    }

    @Test(expectedExceptions = RuntimeException.class)
    public void testSelectingUnknownValue() {
        autocomplete.type("ala").select(ChoicePickerHelper.byVisibleText().endsWith("baster"));
    }

    @Test
    public void testSelectingMultipleValues() {
        autocomplete
            .type("ala").select(ChoicePickerHelper.byIndex().last())
            .type("i").select(1);
        checkOutput("Alaska, Illinois");

        autocompleteAttributes.set(AutocompleteAttributes.tokens, ";");
        autocomplete.advanced().setupToken(";");
        autocomplete.clear();
        Graphene.waitAjax().until().element(autocomplete.advanced().getInput().advanced().getInputElement()).text().equalTo("");

        autocomplete
            .type("m").select(ChoicePickerHelper.byVisibleText().contains("ss"))// selects Massachusetts
            .type("w").select(ChoicePickerHelper.byIndex().index(2))// selects Wisconsin
            .type("k").select();// selects Kansas
        checkOutput("Massachusetts; Wisconsin; Kansas");
    }

    @Test
    public void testSelectingMultipleValuesWithSomeNotListed() {
        Graphene.guardAjax(autocomplete)
            .type("notInListValue").confirm()
            .type("alabama").confirm()
            .type("i").select(1);
        checkOutput("notInListValue, alabama, Illinois");
    }

    @Test
    public void testTypingPrefixAndThenSelectChoiceThatContainsAndEndsWith() {
        autocomplete.type("a").select(ChoicePickerHelper.byVisibleText().contains("b").endsWith("ama"));
        checkOutput("Alabama");
    }

    @Test
    public void testTypingPrefixAndThenSelectFirst() {
        assertTrue(autocomplete.advanced().getSuggestionsElements().isEmpty());
        SelectOrConfirm type = Graphene.guardAjax(autocomplete).type("ala");
        autocomplete.advanced().waitForSuggestionsToBeVisible().perform();
        assertEquals(autocomplete.advanced().getSuggestionsElements().size(), 2, "There should be 2 options");
        Graphene.guardAjax(type).select();
        String expectedStateForPrefix = getExpectedStateForPrefix("ala", true);
        assertEquals(autocomplete.advanced().getInput().getStringValue().toLowerCase(), expectedStateForPrefix.toLowerCase());
        checkOutput(expectedStateForPrefix);
    }
}
