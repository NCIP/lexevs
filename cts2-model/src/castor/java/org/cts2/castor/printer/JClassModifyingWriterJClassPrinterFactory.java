/*
 * Copyright 2005-2008 Werner Guttmann
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.cts2.castor.printer;

import org.exolab.castor.builder.printing.JClassPrinter;
import org.exolab.castor.builder.printing.JClassPrinterFactory;

/**
 * A factory for creating JClassModifyingWriterJClassPrinter objects.
 */
public class JClassModifyingWriterJClassPrinterFactory implements JClassPrinterFactory {

    /** 
     * The name of the factory.
     */
    private static final String NAME = "modifying-writer";

    /**
     * Gets the j class printer.
     *
     * @return the j class printer
     * {@inheritDoc}
     * @see org.exolab.castor.builder.printing.JClassPrinterFactory#getJClassPrinter()
     */
    public JClassPrinter getJClassPrinter() {
        return new JClassModifyingWriterJClassPrinter();
    }

    /**
     * Gets the name.
     *
     * @return the name
     * {@inheritDoc}
     * @see org.exolab.castor.builder.printing.JClassPrinterFactory#getName()
     */
    public String getName() {
        return NAME;
    }
}
