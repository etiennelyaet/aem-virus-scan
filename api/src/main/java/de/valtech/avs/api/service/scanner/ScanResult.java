/*
 * Copyright 2020 Valtech GmbH
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package de.valtech.avs.api.service.scanner;

/**
 * Result of a single file scan.
 * 
 * @author Roland Gruber
 */
public class ScanResult {

    private boolean clean;

    private String output;

    private String path;

    private String userId;

    /**
     * Constructor
     * 
     * @param output command output
     * @param clean  file is clean
     */
    public ScanResult(String output, boolean clean) {
        this.clean = clean;
        this.output = output;
    }

    /**
     * Returns if the file is clean.
     * 
     * @return clean
     */
    public boolean isClean() {
        return clean;
    }

    /**
     * Returns the command output.
     * 
     * @return output
     */
    public String getOutput() {
        return output;
    }

    @Override
    public String toString() {
        return "Clean: " + Boolean.toString(clean) + "\n" + "Output: " + output;
    }

    /**
     * Returns the path of the scanned node.
     * 
     * @return path
     */
    public String getPath() {
        return path;
    }

    /**
     * Sets the path of the scanned node.
     * 
     * @param path path
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Sets the user id.
     * 
     * @param userId user id
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * Returns the user id.
     * 
     * @return user id
     */
    public String getUserId() {
        return userId;
    }

}
