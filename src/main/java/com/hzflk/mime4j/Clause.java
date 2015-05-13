package com.hzflk.mime4j;

import java.io.Serializable;

interface Clause extends Serializable {

    /**
     * Evaluates this clause with the specified chunk of data.
     */
    boolean eval(byte[] data);

    /**
     * Returns the size of this clause. The size of a clause is the number of
     * chars it is composed of.
     */
    int size();

}