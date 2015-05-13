package com.hzflk.mime4j;

import java.util.Arrays;

class AndClause implements Clause {

    private final Clause[] clauses;

    AndClause(Clause... clauses) {
        this.clauses = clauses;
    }

    public boolean eval(byte[] data) {
        for (Clause clause : clauses) {
            if (!clause.eval(data)) {
                return false;
            }
        }
        return true;
    }

    public int size() {
        int size = 0;
        for (Clause clause : clauses) {
            size += clause.size();
        }
        return size;
    }

    public String toString() {
        return "and" + Arrays.toString(clauses);
    }

}