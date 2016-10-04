package uk.ac.ebi.pride.spectracluster.repo.model;

/**
 * @author Yasset Perez-Riverol (ypriverol@gmail.com)
 * @date 04/03/2016
 */
public class AssayReport {

    private Long id;

    private String accession;

    private String species;

    private boolean multiSpecies;

    private String taxonomyId;

    private String projectAccession;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccession() {
        return accession;
    }

    public void setAccession(String accession) {
        this.accession = accession;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public boolean isMultiSpecies() {
        return multiSpecies;
    }

    public void setMultiSpecies(boolean multiSpecies) {
        this.multiSpecies = multiSpecies;
    }

    public String getTaxonomyId() {
        return taxonomyId;
    }

    public void setTaxonomyId(String taxonomyId) {
        this.taxonomyId = taxonomyId;
    }

    public String getProjectAccession() {
        return projectAccession;
    }

    public void setProjectAccession(String projectAccession) {
        this.projectAccession = projectAccession;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AssayReport)) return false;

        AssayReport that = (AssayReport) o;

        return id.equals(that.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
