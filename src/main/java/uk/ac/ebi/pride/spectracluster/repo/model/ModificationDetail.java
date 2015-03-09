package uk.ac.ebi.pride.spectracluster.repo.model;

/**
 * Details of a post translational modification
 *
 * @author Rui Wang
 * @version $Id$
 */
public class ModificationDetail {
    private String accession;
    private String name;
    private int position;

    public String getAccession() {
        return accession;
    }

    public void setAccession(String accession) {
        this.accession = accession;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ModificationDetail)) return false;

        ModificationDetail modificationDetail = (ModificationDetail) o;

        if (position != modificationDetail.position) return false;
        if (accession != null ? !accession.equals(modificationDetail.accession) : modificationDetail.accession != null) return false;
        if (name != null ? !name.equals(modificationDetail.name) : modificationDetail.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = accession != null ? accession.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + position;
        return result;
    }

    @Override
    public String toString() {
        return "PTMDetail{" +
                "accession='" + accession + '\'' +
                ", name='" + name + '\'' +
                ", position=" + position +
                '}';
    }
}
