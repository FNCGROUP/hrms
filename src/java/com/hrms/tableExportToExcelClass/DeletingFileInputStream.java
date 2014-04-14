/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hrms.tableExportToExcelClass;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 * @author jet
 */
public class DeletingFileInputStream extends FileInputStream {
    
    /** The file. */
    protected File file = null;

    /**
     * Instantiates a new deleting file input stream.
     * 
     * @param file
     *            the file
     * @throws FileNotFoundException
     *             the file not found exception
     */
    public DeletingFileInputStream(final File file) throws FileNotFoundException {
        super(file);
        this.file = file;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.io.FileInputStream#close()
     */
    @Override
    public void close() throws IOException {
        super.close();
        file.delete();
    }
    
}
