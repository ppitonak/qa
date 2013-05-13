/**
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.page.fragments.impl.treeNode;

import org.jboss.arquillian.graphene.spi.annotations.Root;
import org.openqa.selenium.WebElement;

/**
 * @author <a href="jjamrich@redhat.com">Jan Jamrich</a>
 *
 */
public class RichFacesTreeNodeIcon {

    public static final String CLASS_ICON_LEAF = "rf-trn-ico-lf";
    public static final String CLASS_ICON_EXPANDED = "rf-trn-ico-exp";
    public static final String CLASS_ICON_COLLAPSED = "rf-trn-ico-colps";

    @Root
    public WebElement root;

    public boolean isLeaf() {
        return root.getAttribute("class").contains(CLASS_ICON_LEAF);
    }

    public boolean isCollapsed() {
        return root.getAttribute("class").contains(CLASS_ICON_COLLAPSED);
    }

    public boolean isExpanded() {
        return root.getAttribute("class").contains(CLASS_ICON_EXPANDED);
    }
}