/**
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
 */
package org.richfaces.tests.metamer.ftest.richItemChangeListener;

import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.extension.configurator.templates.annotation.Templates;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestAccordionItemChangeListener extends AbstractItemChangeListenerTest<ICLAccordionPage> {

    private final String ICL_as_ComponentAttribute_PhaseName = "item changed: item1 -> item2";
    private final String ICL_inComponent_usingType_PhaseName = "itemChangeListenerBean item changed: item1 -> item2";
    private final String ICL_inComponent_usingListener_PhaseName = "itemChangeListenerBean2 item changed: item1 -> item2";
    private final String ICL_outsideComponent_usingType_PhaseName = "itemChangeListenerBean3 item changed: item1 -> item2";

    public TestAccordionItemChangeListener() {
        super("accordion");
    }

    @Test
    @Templates(exclude = { "a4jRepeat", "richCollapsibleSubTable", "richExtendedDataTable", "richDataGrid" })
    public void testICLAsAttribute() {
        super.testICLAsAttributeOfComponent(ICL_as_ComponentAttribute_PhaseName);
    }

    @Test
    @Templates(value = { "a4jRepeat", "richCollapsibleSubTable", "richExtendedDataTable", "richDataGrid" })
    @RegressionTest("https://issues.jboss.org/browse/RF-12173")
    public void testICLAsAttributeInIterationComponents() {
        super.testICLAsAttributeOfComponent(ICL_as_ComponentAttribute_PhaseName);
    }

    @Test
    @Templates(exclude = { "a4jRepeat", "richCollapsibleSubTable", "richExtendedDataTable", "richDataGrid" })
    public void testICLInsideComponentUsingType() {
        super.testICLInComponentWithType(ICL_inComponent_usingType_PhaseName);
    }

    @Test
    @Templates(value = { "a4jRepeat", "richCollapsibleSubTable", "richExtendedDataTable", "richDataGrid" })
    @RegressionTest("https://issues.jboss.org/browse/RF-12173")
    public void testICLInsideComponentUsingTypeInIterationComponents() {
        super.testICLInComponentWithType(ICL_inComponent_usingType_PhaseName);
    }

    @IssueTracking("https://issues.jboss.org/browse/RF-12087")
    @Test(groups = "Future")
    public void testICLInsideComponentUsingListener() {
        super.testICLInComponentWithListener(ICL_inComponent_usingListener_PhaseName);
    }

    @IssueTracking("https://issues.jboss.org/browse/RF-12089")
    @Test(groups = "Future")
    public void testICLOutsideComponentUsingForAndType() {
        super.testICLAsForAttributeWithType(ICL_outsideComponent_usingType_PhaseName);
    }
}
