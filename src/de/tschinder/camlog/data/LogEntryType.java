package de.tschinder.camlog.data;

public enum LogEntryType {
    PRIVATE(0), BUSINESS(1), WORK(2);


    protected long ordinal = 0;

    /**
     * 
     * @param ord
     */
    private LogEntryType(long ord) {
        this.ordinal = ord;
    }

    /**
     * 
     * @param ord
     * @return
     */
    public static LogEntryType byOrdinal(long ord) throws IllegalArgumentException {
        for (LogEntryType m : LogEntryType.values()) {
            if (m.ordinal == ord) {
                return m;
            }
        }
        throw new IllegalArgumentException("Could not find type by ordinal");
    }
}