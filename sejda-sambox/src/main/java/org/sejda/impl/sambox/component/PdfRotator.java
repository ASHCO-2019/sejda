/*
 * Copyright 2015 by Andrea Vacondio (andrea.vacondio@gmail.com).
 *
 * This file is part of the Sejda source code
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.sejda.impl.sambox.component;

import static org.sejda.model.rotation.Rotation.getRotation;

import java.util.Set;

import org.sejda.model.rotation.Rotation;
import org.sejda.sambox.pdmodel.PDDocument;
import org.sejda.sambox.pdmodel.PDPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles rotations on a given PDDocument.
 * 
 * @author Andrea Vacondio
 * 
 */
public final class PdfRotator implements OngoingRotation {

    private static final Logger LOG = LoggerFactory.getLogger(PdfRotator.class);

    private PDDocument document;
    private Rotation rotation;
    private Set<Integer> pages;

    private PdfRotator(Rotation rotation, Set<Integer> pages) {
        this.rotation = rotation;
        this.pages = pages;
    }

    /**
     * DSL entry point to apply a rotation to a set of pages
     * <p>
     * <code>applyRotation(rotation, pages).to(document);</code>
     * </p>
     * 
     * @param rotation
     * @param pages
     * @return the ongoing apply rotation exposing methods to set the document you want to apply the rotation to.
     */
    public static OngoingRotation applyRotation(Rotation rotation, Set<Integer> pages) {
        return new PdfRotator(rotation, pages);
    }

    public void to(PDDocument document) {
        this.document = document;
        executeRotation();
    }

    /**
     * Apply the rotation to the given {@link PDDocument}
     */
    private void executeRotation() {
        LOG.debug("Applying rotation of {} degrees to {} pages", rotation.getDegrees(), pages.size());
        for (int p : pages) {
            apply(p);
        }
    }

    /**
     * apply the rotation to the given page if necessary
     * 
     * @param pageNmber
     */
    private void apply(int pageNmber) {
        if (pages.contains(pageNmber)) {
            PDPage page = (PDPage) document.getDocumentCatalog().getPages().get(pageNmber - 1);
            page.setRotation(rotation.addRotation(getRotation(page.getRotation())).getDegrees());
        }
    }
}