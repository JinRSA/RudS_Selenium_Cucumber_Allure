package stepDefs;

public enum SortBy {
    По_умолчанию("По умолчанию", 101),
    Дешевле("Дешевле", 1),
    Дороже("Дороже", 2),
    По_дате("По дате", 104);

    private String m_by;
    private int m_id;

    public String getBy() { return m_by; }

    public int getId() { return m_id; }

    SortBy(String by, int id) {
        m_by = by;
        m_id = id;
    }
}